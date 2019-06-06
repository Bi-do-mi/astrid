package com.bidomi.astrid.Model;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class UnitImage {
    private String filename = null;
    @Transient
    private String filetype = null;
    @Transient
    private String value = null;

    public UnitImage() {
    }

    public UnitImage(String filename, String filetype, String value) {
        this.filename = filename;
        this.filetype = filetype;
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\nUnitImage{" +
                "filename='" + filename + '\'' +
                ", filetype=" + filetype +
                '}';
    }
}
