package com.bidomi.astrid.Model;

import javax.persistence.*;

@Embeddable
public class Role {
//    @Id
//    @GeneratedValue(generator = "ID_GENERATOR")
//    @Column(name = "role_id", columnDefinition = "BIGINT(20) UNSIGNED")
//    private Long id;
    @Column(length = 20)
    private String role;

    public Role() {
    }

    public Role(String role) {
        this.role = role;

    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}