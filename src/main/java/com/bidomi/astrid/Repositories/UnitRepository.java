package com.bidomi.astrid.Repositories;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Repositories.SpatialQueries.UnitsInPolygon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long>,
        UnitsInPolygon<Unit> {
}
