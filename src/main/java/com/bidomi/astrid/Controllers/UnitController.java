package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.UnitType;
import com.bidomi.astrid.Model.UnitImage;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UnitTypesRepository;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/units")
public class UnitController {

    @Autowired
    private UnitTypesRepository unitTypesRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${units-images-path}")
    private String unitsImagesPath;

    public UnitController() {
        this.unitTypesRepository = unitTypesRepository;
    }

    @PostMapping("/create_unit")
    public @ResponseBody
    User createUnit(@RequestBody Unit unit) {
//        System.out.println("/create_unit: \n" + unit);
        User user = this.userRepository.findById(unit.getOuner().getId()).get();
        unit.setCreatedOn(DateTime.now());
        unit.setLastUpdate(DateTime.now());
        user.addUnit(unit);
        user.setLastVisit(DateTime.now());
        user = userRepository.save(user);

        if (!unit.getImages().isEmpty()) {
            File dir = new File(unitsImagesPath);
            if (!dir.exists()) dir.mkdir();
            ArrayList<Unit> unitList = new ArrayList<>(user.getUnits());
            Collections.sort(unitList, new Comparator<Unit>() {
                public int compare(Unit u1, Unit u2) {
                    if (u1 == null || u2 == null || u1.getLastUpdate() == null
                            || u2.getLastUpdate() == null) {
                        return 0;
                    }
                    if (u1.getLastUpdate().isAfter(u2.getLastUpdate())) {
                        return 1;
                    }
                    if (u1.getLastUpdate().equals(u2.getLastUpdate())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            Unit modifiedUnit = unitList.get(unitList.size() - 1);
            for (int p = 0; p < modifiedUnit.getImages().size(); p++) {
                UnitImage incomingImage = new ArrayList<UnitImage>(
                        unit.getImages()).get(p);
                UnitImage i = new ArrayList<UnitImage>(
                        modifiedUnit.getImages()).get(p);
                i.setFilename(DateTime.now().getMillis() + "_" + modifiedUnit.getId()
                        + "_" + i.getFilename());
                try {
                    BASE64Decoder decoder = new BASE64Decoder();
                    ByteArrayInputStream bis = new ByteArrayInputStream(
                            (byte[]) decoder.decodeBuffer(incomingImage.getValue()));
                    BufferedImage image = ImageIO.read(bis);
                    bis.close();
                    File outputFile = new File(unitsImagesPath + i.getFilename());
                    ImageIO.write(image, "jpg", outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    modifiedUnit.getImages().remove(i);
                }
            }
            user = userRepository.save(user);
        }
        return user;
    }

    @PostMapping("/update_unit")
    public @ResponseBody
    User updateUnit(@RequestBody Unit unit) {
//        System.out.println("/update: \n" + unit);
        User user = this.userRepository.findById(unit.getOuner().getId()).get();
        user.getUnits().forEach(u -> {
            if (u.getId() == unit.getId()) {
                u.setLastUpdate(DateTime.now());
                u.setType(unit.getType());
                u.setBrand(unit.getBrand());
                u.setModel(unit.getModel());
                u.setLocation(unit.getLocation());
                u.setEnabled(unit.isEnabled());
                u.setPaid(unit.isPaid());
                u.setTestFor(unit.isTestFor());
                u.setCreatedOn(unit.getCreatedOn());
                u.setPaidUntil(unit.getPaidUntil());
                u.setOptions(unit.getOptions());

                ArrayList<UnitImage> oldImagesList = new ArrayList<>(u.getImages());
                if (unit.getImages().size() > 0) {
                    unit.getImages().forEach(img -> {
                        boolean containes = false;
                        for (UnitImage old : oldImagesList) {
                            System.out.println("old.getFilename()"+old.getFilename());
                            System.out.println("img.getFilename()"+img.getFilename());
                            if (old.getFilename().equals(img.getFilename())) {
                                containes = true;
                            }
                            System.out.println("containes: " + containes);
                        }
                        if (!containes) {
                            System.out.println("Triggered if (!oldImagesList.contains(img)) ");
                            img.setFilename(DateTime.now().getMillis() + "_" + u.getId() +
                                    "_" + img.getFilename());
                        }
                    });
                }
                ArrayList<UnitImage> newImagesList = new ArrayList<>(unit.getImages());
//                стираем удаленные фото из папки
                if (oldImagesList.size() > 0) {
                    oldImagesList.forEach(oldI -> {
                        if (!newImagesList.contains(oldI)) {
                            File deleteFile = new File(unitsImagesPath + oldI.getFilename());
                            deleteFile.delete();
                        }
                    });
                }
//                звписываем новые фото в папку
                if (!newImagesList.isEmpty()) {
                    newImagesList.forEach(newI -> {
                        if (!oldImagesList.contains(newI)) {
                            try {
                                BASE64Decoder decoder = new BASE64Decoder();
                                ByteArrayInputStream bis = new ByteArrayInputStream(
                                        (byte[]) decoder.decodeBuffer(newI.getValue()));
                                BufferedImage image = ImageIO.read(bis);
                                bis.close();
                                File outputFile = new File(unitsImagesPath + newI.getFilename());
                                ImageIO.write(image, "jpg", outputFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                                unit.getImages().remove(newI);
                            }
                        }
                    });
                }
                u.setImages(unit.getImages());
            }
        });
        user.setLastVisit(DateTime.now());
        user = userRepository.save(user);
        return user;
    }

    @PostMapping("/delete_unit")
    public @ResponseBody
    User deleteUnit(@RequestBody Unit unit) {
//        System.out.println("/delete_unit: \n" + unit);
        try {
            User user = this.userRepository.findById(unit.getOuner().getId()).get();
            user.getUnits().remove(unit);
            if (unit.getImages().size() > 0) {
                unit.getImages().forEach(i -> {
                    File deleteFile = new File(unitsImagesPath + i.getFilename());
                    if (deleteFile.delete()) {
                        System.out.println("Deleted: " + i.getFilename());
                    }
                });
            }
            user.setLastVisit(DateTime.now());
            user = userRepository.save(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create_unit_types_list")
    public void createUnitTypesList(
            @RequestBody ArrayList<JsonNode> unitTypesList) {
        unitTypesRepository.deleteAll();
        unitTypesList.forEach((JsonNode tp) -> {
            this.unitTypesRepository.save(new UnitType(tp));
        });
//        unitTypesList.forEach((JsonNode ass) -> {
//            System.out.println(ass + "\n");
//        });
    }

    @GetMapping("/get_unit_types_list")
    public @ResponseBody
    ArrayList<JsonNode> getUnitTypesList() {
//        System.out.println(this.unitTypesRepository.findAll());
        ArrayList<JsonNode> list = new ArrayList<JsonNode>();
        this.unitTypesRepository.findAll().forEach(ass -> {
            list.add(ass.getUnitType());
        });
        return list;
    }
}
