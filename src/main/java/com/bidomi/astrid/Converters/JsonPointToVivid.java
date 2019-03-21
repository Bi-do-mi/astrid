package com.bidomi.astrid.Converters;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.bidomi.astrid.Model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class JsonPointToVivid extends StdConverter<JsonNode, com.vividsolutions.jts.geom.Geometry> {

    @Override
    public com.vividsolutions.jts.geom.Geometry convert(JsonNode sPoint) {

//        System.out.println("From JsonPointToVivid: sPoint - " + sPoint);
        WKTReader wktReader = new WKTReader();
        GeoJSONReader reader = new GeoJSONReader();
        GeometryFactory gf = new GeometryFactory();
        org.locationtech.jts.geom.GeometryFactory lGf = new org.locationtech.jts.geom.GeometryFactory();
        try {
            if (sPoint != null) {
                Geometry geometry = reader.read(sPoint.get("geometry").toString());
//                System.out.println("From JsonPointToVivid: geometry - " + geometry);
                com.vividsolutions.jts.geom.Geometry point = wktReader.read(geometry.toString());
//                System.out.println("From JsonPointToVivid: pp - " + point);
                return point;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}