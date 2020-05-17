package com.home.test.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String login;

    @Column(name = "password_hash")
    private String passwordHash;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Set<RequestEntity> requests;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<RequestEntity> getRequests() {
        return requests;
    }

    public void setRequests(Set<RequestEntity> requests) {
        this.requests = requests;
    }
}
