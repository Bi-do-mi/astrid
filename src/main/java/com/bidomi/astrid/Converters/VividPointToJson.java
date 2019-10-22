package com.bidomi.astrid.Converters;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Point;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.Feature;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.HashMap;

public class VividPointToJson extends StdConverter<Point, Feature> {

   GeoJSONWriter geoJSONWriter = new GeoJSONWriter();
    GeoJSONReader reader = new GeoJSONReader();

    @Override
    public Feature convert(Point point) {
        if (point != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JtsModule());
                Geometry g = reader.read(mapper.writeValueAsString(point));
                Feature geojson = new Feature(geoJSONWriter.write(g),
                        new HashMap<String, Object>());
//                System.out.println("VividPointToJson: \n" + geojson);
                return geojson;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
