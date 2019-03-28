package com.bidomi.astrid.Converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Geometry;
import org.locationtech.jts.geom.Coordinate;
import org.wololo.geojson.Feature;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.HashMap;

public class VividPointToJson extends StdConverter<Geometry, Feature> {

    org.locationtech.jts.geom.GeometryFactory lGf = new org.locationtech.jts.geom.GeometryFactory();
    GeoJSONWriter geoJSONWriter = new GeoJSONWriter();

    @Override
    public Feature convert(Geometry point) {
        if (point != null) {
            try {
                org.locationtech.jts.geom.Geometry geometry = lGf.createPoint(
                        new Coordinate(point.getCoordinate().x, point.getCoordinate().y));
                return new Feature(geoJSONWriter.write(geometry), new HashMap<String, Object>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
