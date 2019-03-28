package com.bidomi.astrid.Converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.locationtech.jts.geom.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;

public class JsonPointToVivid extends StdConverter<JsonNode, com.vividsolutions.jts.geom.Geometry> {

    @Override
    public com.vividsolutions.jts.geom.Geometry convert(JsonNode sPoint) {

//        System.out.println("From JsonPointToVivid: sPoint - " + sPoint);
        WKTReader wktReader = new WKTReader();
        GeoJSONReader reader = new GeoJSONReader();
        try {
            if (sPoint != null) {
                Geometry geometry = reader.read(sPoint.get("geometry").toString());
                com.vividsolutions.jts.geom.Geometry point = wktReader.read(geometry.toString());
                return point;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}