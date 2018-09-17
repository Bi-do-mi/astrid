package com.bidomi.astrid.Model;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;
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