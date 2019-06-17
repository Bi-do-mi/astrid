package com.bidomi.astrid.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "unit_types")
@TypeDef(
        name = "jsonb-node",
        typeClass = JsonNodeBinaryType.class
)
public class UnitType {
    @Id
    @GeneratedValue(generator = "UNIT_TYPES_ID_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Type(type = "jsonb-node")
    @Column(columnDefinition = "jsonb")
    private JsonNode unitType;

    public UnitType() {
    }

    public UnitType(JsonNode uType) {
        this.unitType = uType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonNode getUnitType() {
        return unitType;
    }

    public void setUnitType(JsonNode unitType) {
        this.unitType = unitType;
    }
}
