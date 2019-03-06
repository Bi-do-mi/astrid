package com.bidomi.astrid.Model;


import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
@Table(name = "BASE_POINT")
public class BasePoint {
    @Id
    @Column(name = "POINT_ID", columnDefinition = "BIGINT(20) UNSIGNED")
    @GeneratedValue (generator = "ID_GENERATOR")
    private Long id;

    @Column(name="OUNER_ID", columnDefinition = "BIGINT(20) UNSIGNED")
    private Long ounerId;

    @Column(name="POINT_ROLE", length = 20)
    private String pointRole;

    @Column(name = "LNG")
    private Double lng;

    @Column(name = "LAT")
    private Double lat;

}