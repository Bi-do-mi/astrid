package com.bidomi.astrid.Converters;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Point;
import org.wololo.jts2geojson.GeoJSONReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class JsonPointToVivid extends StdConverter<JsonNode, Point> {

    @Override
    public Point convert(JsonNode json) {

        GeoJSONReader reader = new GeoJSONReader();
        if (json != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JtsModule());
                InputStream in = new ByteArrayInputStream(
                        json.toString().getBytes(Charset.forName("UTF-8")));
                Point point = mapper.readValue(json.get("geometry").toString(), Point.class);
//                System.out.println("JsonPointToVivid: \n" + point);
                return point;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}