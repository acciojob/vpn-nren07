package com.driver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table
public class Connection {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user;
}
