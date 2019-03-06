package com.bidomi.astrid.Model;

import lombok.Data;

@Data
public class Unit {
    private Long id;
    private Long ounerId;
    private String brand;
    private String model;
    private String subModel;
    private BasePoint location;
    private boolean enabled;
}
