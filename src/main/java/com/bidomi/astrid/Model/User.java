package com.bidomi.astrid.Model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "USERS")
@DynamicInsert
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "USER_ID", columnDefinition = "BIGINT(20) UNSIGNED")
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
//    private List<Role> roles;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ROLE")
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "ROLE_ID"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = "ID_GENERATOR")
    private Collection<Role> roles = new ArrayList<Role>();

    @Column(length = 255, nullable = false)
    private String name;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    @Column(name = "registration_date", columnDefinition = "BIGINT(20) UNSIGNED")
    private Long registrationDate;
    @Column(name = "last_visit", columnDefinition = "BIGINT(20) UNSIGNED")
    private Long lastVisit;
    @Column(name = "confirmation_token", length = 255)
    private String confirmationToken;
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "USER_BPOINT",
            joinColumns = @JoinColumn(name = "USER_ID",
                    nullable = false, unique = true),
            inverseJoinColumns =
            @JoinColumn(name = "POINT_ID"))
    private BasePoint basePoint;

    private Collection<Unit> units = new ArrayList<Unit>();

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
        this.basePoint = user.getBasePoint();
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

    public BasePoint getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(BasePoint basePoint) {
        this.basePoint = basePoint;
    }

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, name='%s', EMail='%s']",
                id, name, username);
    }
}
