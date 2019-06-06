package com.bidomi.astrid.Model;

import com.bidomi.astrid.Converters.JsonPointToVivid;
import com.bidomi.astrid.Converters.UserIdToUser;
import com.bidomi.astrid.Converters.UserToUserId;
import com.bidomi.astrid.Converters.VividPointToJson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
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
    private User ouner;
    @Column(nullable = false, length = 40)
    private String assignment;
    @Column(nullable = false, length = 40)
    private String type;
    @Column(nullable = false, length = 40)
    private String brand;
    @Column(nullable = false, length = 40)
    private String model;
    @JsonSerialize(converter = VividPointToJson.class)
    @JsonDeserialize(converter = JsonPointToVivid.class)
    private Geometry location;
    private boolean enabled;
    private boolean paid;
    private boolean testFor;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "unit_images")
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "filename_id"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = "ID_GENERATOR")
    private Collection<UnitImage> images = new ArrayList<UnitImage>();
    @Column(name = "created_on", nullable = false, updatable = false,
            columnDefinition = "timestamp with time zone")
    private DateTime createdOn;
    @Column(name = "last_update", nullable = false, updatable = false,
            columnDefinition = "timestamp with time zone")
    private DateTime lastUpdate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit )) return false;
        return id != null && id.equals(((Unit) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }
    @PreRemove
    public void removeImages(){
        System.out.println("removeImages() triggered!");
    }

    @Override
    public String toString() {
        return "\nUnit{" +
                "\nid=" + id +
                ", \nouner=" + ouner.getId() +
                ", \nassignment='" + assignment + '\'' +
                ", \ntype='" + type + '\'' +
                ", \nbrand='" + brand + '\'' +
                ", \nmodel='" + model + '\'' +
                ", \nlocation=" + location +
                ", \nenabled=" + enabled +
                ", \npaid=" + paid +
                ", \ntestFor=" + testFor +
                ", \ncreatedOn=" + createdOn +
                ", \nlastUpdate=" + lastUpdate +
                ", \nimages=" + images +
                '}';
    }
}
