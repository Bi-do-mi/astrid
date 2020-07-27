package com.bidomi.astrid.Repositories.SpatialQueries;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.User;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.vividsolutions.jts.geom.Geometry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UnitsInPolygonImpl implements UnitsInPolygon {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getUnitsWithinPolygon(Geometry polygon) {
        Query query = em.createQuery("select u from Unit u" +
                " where within(u.location, :filter) = true", Unit.class);
        query.setParameter("filter", polygon);
        return new ArrayList(Collections2.filter(((List<Unit>)query.getResultList()),
                new Predicate<Unit>() {
                    @Override
                    public boolean apply(Unit unit) {
                        return unit.getOwnerId().isEnabled();
                    }
                }));
    }
}
