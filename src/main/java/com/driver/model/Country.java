package com.driver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Country {
    @Id
    private Integer id;
    private CountryName countryName;
    private String code;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private User user;
}
