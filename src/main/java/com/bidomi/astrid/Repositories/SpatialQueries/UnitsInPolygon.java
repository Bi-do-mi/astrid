package com.bidomi.astrid.Repositories.SpatialQueries;

import com.vividsolutions.jts.geom.Geometry;

import java.util.List;

public interface UnitsInPolygon<T> {
    List<T> getUnitsWithinPolygon(Geometry polygon);
}
