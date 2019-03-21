package com.bidomi.astrid.Model;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bidomi.astrid.Converters.JsonPointToVivid;
import com.bidomi.astrid.Converters.VividPointToJson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
//    @Column(name = "user_id", columnDefinition = "BIGINT(20) UNSIGNED")
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, unique = true, length = 60)
    private String username;

//    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
//    @JoinTable(name = "user_role", joinColumns =
//    @JoinColumn(name = "user_id"), inverseJoinColumns =
//    @JoinColumn(name = "role_id"))
//    @Column(nullable = false)
//    private Collection<Role> roles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role")
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "role_id"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = "ID_GENERATOR")
    private Collection<Role> roles = new ArrayList<Role>();

    @Column(length = 255, nullable = false)
    private String name;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    //    @Column(name = "registration_date", columnDefinition = "BIGINT(20) UNSIGNED")
    @Column(name = "registration_date")
    private Long registrationDate;
    //    @Column(name = "last_visit", columnDefinition = "BIGINT(20) UNSIGNED")
    @Column(name = "last_visit")
    private Long lastVisit;
    @Column(name = "confirmation_token", length = 255)
    private String confirmationToken;
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
    @JsonSerialize(converter = VividPointToJson.class)
    @JsonDeserialize(converter = JsonPointToVivid.class)
    private Geometry location;

//    private Collection<Unit> units = new ArrayList<Unit>();

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.roles = user.getRoles();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.enabled = user.isEnabled();
        this.lastVisit = user.getLastVisit();
        this.confirmationToken = user.getConfirmationToken();
        this.phoneNumber = user.getPhoneNumber();
        this.location = user.getLocation();
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Long lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Geometry getLocation() {
        return location;
    }

    public void setLocation(Geometry location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "User{" +
                "\nid=" + id +
                ", \npassword='" + password + '\'' +
                ", \nusername='" + username + '\'' +
                ", \nroles=" + roles +
                ", \nname='" + name + '\'' +
                ", \naccountNonExpired=" + accountNonExpired +
                ", \naccountNonLocked=" + accountNonLocked +
                ", \ncredentialsNonExpired=" + credentialsNonExpired +
                ", \nenabled=" + enabled +
                ", \nregistrationDate=" + registrationDate +
                ", \nlastVisit=" + lastVisit +
                ", \nconfirmationToken='" + confirmationToken + '\'' +
                ", \nphoneNumber='" + phoneNumber + '\'' +
                ", \nlocation=" + location +
                '}';
    }
}
