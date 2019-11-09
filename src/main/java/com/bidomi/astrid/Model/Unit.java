package com.bidomi.astrid.Model;

import com.bidomi.astrid.Converters.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Unit {

    @Id
    @GeneratedValue(generator = "UNIT_ID_GENERATOR")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonSerialize(converter = UserToUserId.class)
    @JsonDeserialize(converter = UserIdToUser.class)
    private User ownerId;
    @Column(nullable = false, length = 40)
    private String type;
    @Column(nullable = false, length = 40)
    private String brand;
    @Column(nullable = false, length = 40)
    private String model;
    @JsonSerialize(converter = VividPointToJson.class)
    @JsonDeserialize(converter = JsonPointToVivid.class)
    private Point location;
    private boolean enabled;
    private boolean paid;
    private boolean testFor;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "unit_images")
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "filename_id"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = "UNIT_IMAGE_ID_GENERATOR")
    @JsonSerialize(converter = ImageValue.class)
    private Collection<UnitImage> images = new ArrayList<UnitImage>();

    @Column(name = "created_on", nullable = false, updatable = false,
            columnDefinition = "timestamp with time zone")
    private DateTime createdOn;

    @Column(name = "last_update", nullable = false,
            columnDefinition = "timestamp with time zone")
    private DateTime lastUpdate;

    @Column(name = "paid_until", nullable = true, updatable = true,
            columnDefinition = "timestamp with time zone")
    private DateTime paidUntil;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "unit_options")
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "option_id"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = "UNIT_OPTIONS_ID_GENERATOR")
    private Collection<UnitOption> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isTestFor() {
        return testFor;
    }

    public void setTestFor(boolean testFor) {
        this.testFor = testFor;
    }

    public Collection<UnitImage> getImages() {
        return images;
    }

    public void setImages(Collection<UnitImage> images) {
        this.images = images;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public DateTime getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(DateTime paidUntil) {
        this.paidUntil = paidUntil;
    }

    public Collection<UnitOption> getOptions() {
        return options;
    }

    public void setOptions(Collection<UnitOption> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        return id != null && id.equals(((Unit) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

//    @PreRemove
//    public void removeImages() {
//        System.out.println("removeImages: ");
//        if (images.size() > 0) {
//            System.out.println("if (images.size()>0)");
//            images.forEach(i -> {
//                System.out.println("i: " + unitsImagesPath + i.getFilename());
//                File deleteFile = new File(unitsImagesPath + i.getFilename());
//                if (deleteFile.delete()) {
//                    System.out.println("Deleted: " + i.getFilename());
//                }
//            });
//        }
//    }

    @Override
    public String toString() {
        return "\nUnit{" +
                "\nid=" + id +
                ", \nownerId=" + ownerId.getId() +
                ", \ntype='" + type + '\'' +
                ", \nbrand='" + brand + '\'' +
                ", \nmodel='" + model + '\'' +
                ", \nlocation=" + location +
                ", \nenabled=" + enabled +
                ", \npaid=" + paid +
                ", \ntestFor=" + testFor +
                ", \ncreatedOn=" + createdOn +
                ", \nlastUpdate=" + lastUpdate +
                ", \npaidUntil=" + paidUntil +
                ", \nimages=" + images +
                '}';
    }
}
