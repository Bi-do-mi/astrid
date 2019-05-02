package com.bidomi.astrid.Converters;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.jts2geojson.GeoJSONReader;

public class UserIdToUser extends StdConverter<JsonNode, User> {
    @Autowired
    UserRepository userRepository;

    @Override
    public User convert(JsonNode jsonNode) {
//        System.out.println(jsonNode);
        return userRepository.findById(jsonNode.asLong()).get();

    }
}
