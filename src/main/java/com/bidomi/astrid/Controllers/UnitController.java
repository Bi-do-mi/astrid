package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.UnitAssignment;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UnitTypesRepository;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/units")
public class UnitController {

    @Autowired
    private UnitTypesRepository unitTypesRepository;
    @Autowired
    private UserRepository userRepository;

    public UnitController() {
        this.unitTypesRepository = unitTypesRepository;
    }

    @PostMapping("/create_unit")
    public @ResponseBody User createUnit(@RequestBody Unit unit) {
        User user = this.userRepository.findById(unit.getOuner().getId()).get();
        user.addUnit(unit);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create_unit_types_list")
    public void createUnitTypesList(
            @RequestBody ArrayList<JsonNode> unitTypesList) {
        unitTypesRepository.deleteAll();
        unitTypesList.forEach((JsonNode ass) -> {
            this.unitTypesRepository.save(new UnitAssignment(ass));
        });
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
