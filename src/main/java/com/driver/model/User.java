package com.driver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String originalIp;
    private String maskedIp;
    private boolean connected;

    @ManyToMany
    @JoinColumn
    @JsonIgnore
    List<ServiceProvider> serviceProviderList=new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JoinColumn
    private Country country;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Connection>connectionList=new ArrayList<>();
}
