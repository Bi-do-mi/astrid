package com.bidomi.astrid.Repositories.SpatialQueries;

import com.vividsolutions.jts.geom.Geometry;

import java.util.List;

public interface UsersInPolygon<T> {
    List<T> getUsersWithinPolygon(Geometry polygon);
}
