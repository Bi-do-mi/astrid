package com.bidomi.astrid.Model;

import lombok.Data;
import org.geolatte.geom.Point;

import javax.persistence.Entity;

@Data
//@Entity
public class Unit {
    private Long id;
    private Long ounerId;
    private String brand;
    private String model;
    private String subModel;
    private Point location;
    private boolean enabled;
}
