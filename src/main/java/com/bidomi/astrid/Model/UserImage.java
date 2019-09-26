package com.bidomi.astrid.Model;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class UserImage {
    private String filename = null;
    @Transient
    private String filetype = null;
    @Transient
    private String value = null;

    public UserImage() {
    }

    public UserImage(String filename, String filetype, String value) {
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
        return "\nUserImage{" +
                "filename='" + filename + "\'" +
                ", \nfiletype=" + filetype + "\'" +
//                ", \nvalue=" + value +
                '}';
    }
}
