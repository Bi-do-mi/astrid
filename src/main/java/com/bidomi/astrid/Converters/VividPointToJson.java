package com.bidomi.astrid.Converters;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.IOException;

public class VividPointToJson extends StdConverter<Point, String> {

    ObjectMapper mapper;
    String result;

    public VividPointToJson() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
    }

    @Override
    public String convert(Point point) {
        if (point != null) {
            try {
                result = mapper.writeValueAsString(point);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//            System.out.println("From VividPointToJson: " + result);
            return result;
        }
        return null;
    }
}
