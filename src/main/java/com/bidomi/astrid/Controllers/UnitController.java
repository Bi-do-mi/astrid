package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.UnitAssignment;
import com.bidomi.astrid.Model.UnitImage;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UnitTypesRepository;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
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

        User user = this.userRepository.findById(unit.getOuner().getId()).get();
        unit.setCreatedOn(DateTime.now());
        unit.setLastUpdate(DateTime.now());
        user.addUnit(unit);
        user.setLastVisit(DateTime.now());
        user = userRepository.save(user);

        if (!unit.getImages().isEmpty()) {
            try {
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
                    BASE64Decoder decoder = new BASE64Decoder();
                    ByteArrayInputStream bis = new ByteArrayInputStream(
                            (byte[]) decoder.decodeBuffer(incomingImage.getValue()));
                    BufferedImage image = ImageIO.read(bis);
                    bis.close();
                    File outputFile = new File(unitsImagesPath + i.getFilename());
                    ImageIO.write(image, "jpg", outputFile);
                }
                user = userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(currentPrincipalName).get();

        return user;
    }

    @PostMapping("/save_unit_image")
    public @ResponseBody
    User saveUnitImage(@RequestParam(value = "photo", required = false)
                               List<MultipartFile> images) {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        if (images.size() > 0) {
            saveImageToDir(images);
        }
        return user;
    }

    void saveImageToDir(List<MultipartFile> images) {

        System.out.println("In saveImageToDir");
        System.out.println("From saveImageToDir: \n" + images.get(0).getOriginalFilename()
                + images.get(0).getName() + "\n" + images.get(0).getContentType());
//        for (MultipartFile image : images) {

//            try {
//                byte[] bytes = image.getBytes();
////                File file = new File("../" + image.getOriginalFilename());
//                System.out.println("From saveImageToDir: \n" + image.getOriginalFilename()
//                        + image.getName() + "\n" + image.getContentType() +
//                        "\n" + image.getBytes());
////            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((new FileOutputStream(file)));
////            bufferedOutputStream.write(bytes);
////            bufferedOutputStream.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };


    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create_unit_types_list")
    public void createUnitTypesList(
            @RequestBody ArrayList<JsonNode> unitTypesList) {
        unitTypesRepository.deleteAll();
        unitTypesList.forEach((JsonNode ass) -> {
            this.unitTypesRepository.save(new UnitAssignment(ass));
        });
//        unitTypesList.forEach((JsonNode ass) -> {
//            System.out.println(ass + "\n");
//        });
    }

    @GetMapping("/get_unit_types_list")
    public @ResponseBody
    ArrayList<JsonNode> getUnitTypesList() {
        System.out.println(this.unitTypesRepository.findAll());
        ArrayList<JsonNode> list = new ArrayList<JsonNode>();
        this.unitTypesRepository.findAll().forEach(ass -> {
            list.add(ass.getAssignment());
        });
        return list;
    }
}
