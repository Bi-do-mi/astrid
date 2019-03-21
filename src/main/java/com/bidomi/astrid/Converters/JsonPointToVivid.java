package com.bidomi.astrid.Converters;

import com.bidomi.astrid.Model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

import java.io.IOException;
import java.io.StringReader;

public class JsonPointToVivid extends StdConverter<JsonNode, Geometry> {

    @Override
    public Point convert(JsonNode sPoint) {
        System.out.println("From JsonPointToVivid: " + sPoint);
        try {
            if (sPoint != null) {
                GeometryFactory gf = new GeometryFactory();
                Point point = gf.createPoint(
                        new Coordinate(sPoint.get("coordinates").asDouble()));
                System.out.println("From JsonPointToVivid: " + point);
                return point;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}