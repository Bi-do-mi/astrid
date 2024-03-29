package com.bidomi.astrid.Repositories.SpatialQueries;

import com.bidomi.astrid.Model.User;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.vividsolutions.jts.geom.Geometry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UsersInPolygonImpl implements UsersInPolygon {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getUsersWithinPolygon(Geometry polygon) {
        Query query = em.createQuery("select u from User u" +
                " where within(u.location, :filter) = true", User.class);
        query.setParameter("filter", polygon);
        return new ArrayList(Collections2.filter(((List<User>)query.getResultList()),
                new Predicate<User>() {
                    @Override
                    public boolean apply(User user) {
                        return user.isEnabled();
                    }
                }));
    }
}
