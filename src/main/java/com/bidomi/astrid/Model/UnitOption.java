package com.bidomi.astrid.Model;

import javax.persistence.Embeddable;

@Embeddable
public class UnitOption {
    String label = "";
    String value = "";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
