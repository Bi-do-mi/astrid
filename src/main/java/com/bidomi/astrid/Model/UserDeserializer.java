package com.bidomi.astrid.Model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.io.IOException;

public class UserDeserializer extends StdDeserializer<User> {

    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser parser, DeserializationContext ctxt) {
        Point<G2D> p = null;
        User user = new User();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectCodec codec = parser.getCodec();
            ObjectNode node = codec.readTree(parser);
            if (node.get("location").get("x") != null) {
                p = Geometries.mkPoint(new G2D(node.get("location").get("x").asDouble(),
                        node.get("location").get("y").asDouble()), CoordinateReferenceSystems.WGS84);
            }
            node.remove("location");
            user = mapper.treeToValue(node, User.class);
            user.setLocation(p);
            System.out.println("UserDeserializer: " + user.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
}
