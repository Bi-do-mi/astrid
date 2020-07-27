package com.bidomi.astrid.Controllers;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UnitRepository;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/search")
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Value("${serverLocDir}")
    private String serverLocDir;

    public SearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    @PreAuthorize("permitAll")
    @PostMapping("/on_moveend")
    public List<User> onMoveEnd(@RequestBody JsonNode mPolygon) {
//        System.out.println("Polygon: \n" + mPolygon);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        if (mPolygon.get("geometry").get("type").asText().equals("MultiPolygon")) {
//            System.out.println("type=MultiPolygon\n" + mPolygon);
            try {
                MultiPolygon multiPolygon = mapper.readValue(
                        mPolygon.get("geometry").toString(), MultiPolygon.class);
                final List resp = new ArrayList<ArrayList>();
                resp.add(userRepository.getUsersWithinPolygon(multiPolygon));
                resp.add(unitRepository.getUnitsWithinPolygon(multiPolygon));
                return resp;
            } catch (IOException e) {
                System.out.println("!!!\n"
                        + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @PostMapping("/on_recheck_other_units")
    @ResponseBody
    public List<Unit> onRecheckOtherUnits(@RequestBody List<Long> checkUnits) {
        final List<Unit> checkedUnits = new ArrayList();
        checkUnits.forEach((unitId) -> {
            Unit u = unitRepository.findById(unitId).get();
            if (u.getWorkEnd().isBeforeNow()) {
                User user = userRepository.findById(u.getOwnerId().getId()).get();
                u.setLocation(user.getLocation());
                u.setWorkEnd(null);
                unitRepository.save(u);
                checkedUnits.add(u);
            }
        });
        return checkedUnits;
    }

    @GetMapping("/on_recheck_oun_units")
    @ResponseBody
    public User onRecheckOunUnits() {
        User user = userRepository.findByUsername(SecurityContextHolder
                .getContext().getAuthentication().getName()).get();
        user.getUnits().forEach((u) -> {
            if (u.getWorkEnd() != null && u.getWorkEnd().isBeforeNow()) {
                u.setLocation(user.getLocation());
                u.setWorkEnd(null);
            }
        });
        return user;
    }

    @PutMapping("get_owner")
    @ResponseBody
    public User getOner(@RequestBody Long ownerId) {
        User u = userRepository.findById(ownerId).get();
        return u.isEnabled() ? u : null;
    }
}
