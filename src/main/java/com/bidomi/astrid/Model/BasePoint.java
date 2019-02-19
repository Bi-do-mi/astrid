package com.bidomi.astrid.Model;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "BASE_POINT")
public class BasePoint {
    @Id
    @Column(name = "POINT_ID", columnDefinition = "BIGINT(20) UNSIGNED")
    @GeneratedValue (generator = "ID_GENERATOR")
    private Long id;

    private Point point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_BPOINT",
            joinColumns = @JoinColumn(name = "POINT_ID"),
            inverseJoinColumns =
            @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    )
    private User userRef;

    public BasePoint() {
    }

    public BasePoint(User userRef) {
        this.userRef = userRef;
    }

    public BasePoint(User userRef, Point point) {
        this.point = point;
        this.userRef = userRef;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }


    public Long getId() {
        return id;
    }

    public User getUserRef() {
        return userRef;
    }

    public void setUserRef(User userRef) {
        this.userRef = userRef;
    }
}