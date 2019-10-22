package com.bidomi.astrid.Controllers;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Polygon;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/search")
public class SearchController {
    private UserRepository userRepository;

    public SearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/on_moveend")
    public List<User> onMoveEnd(@RequestBody JsonNode polygon) {
        if (polygon.get("geometry").get("type").asText().equals("Polygon")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JtsModule());
                InputStream in = new ByteArrayInputStream(
                        polygon.toString().getBytes(Charset.forName("UTF-8")));
                Polygon polygon_ = mapper.readValue(polygon.get("geometry").toString(), Polygon.class);
                return userRepository.getUsersWithinPolygon(polygon_);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
