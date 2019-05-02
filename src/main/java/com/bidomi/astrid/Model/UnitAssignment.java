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
public class UnitAssignment {
    @Id
    @GeneratedValue(generator = "UNIT_TYPES_ID_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Type(type = "jsonb-node")
    @Column(columnDefinition = "jsonb")
    private JsonNode assignment;

    public UnitAssignment() {
    }

    public UnitAssignment(JsonNode assignment) {
        this.assignment = assignment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonNode getAssignment() {
        return assignment;
    }

    public void setAssignment(JsonNode assignment) {
        this.assignment = assignment;
    }
}
