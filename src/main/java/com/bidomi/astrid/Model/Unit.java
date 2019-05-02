package com.bidomi.astrid.Model;

import com.bidomi.astrid.Converters.JsonPointToVivid;
import com.bidomi.astrid.Converters.UserIdToUser;
import com.bidomi.astrid.Converters.UserToUserId;
import com.bidomi.astrid.Converters.VividPointToJson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
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
}
