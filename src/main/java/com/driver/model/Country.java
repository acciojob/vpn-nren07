package com.driver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(value = EnumType.STRING)
    private CountryName countryName;
    private String code;

    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;

    @OneToOne
    @JoinColumn
    private User user;

    public Country() {
    }

    public Country(Integer id, CountryName countryName, String code, ServiceProvider serviceProvider, User user) {
        this.id=id;
        this.countryName = countryName;
        this.code = code;
        this.serviceProvider = serviceProvider;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void enrich(String countryName) throws Exception{
        String updatedName=countryName.toUpperCase();

        if(updatedName.equals("IND")){
            this.setCountryName(CountryName.IND);
            this.setCode(CountryName.IND.toCode());
        }
        else if(updatedName.equals("USA")){
            this.setCountryName(CountryName.USA);
            this.setCode(CountryName.USA.toCode());
        }
        else if(updatedName.equals("AUS")){
            this.setCountryName(CountryName.AUS);
            this.setCode(CountryName.AUS.toCode());
        }
        else if(updatedName.equals("CHI")){
            this.setCountryName(CountryName.CHI);
            this.setCode(CountryName.CHI.toCode());
        }
        else if(updatedName.equals("JPN")){
            this.setCountryName(CountryName.JPN);
            this.setCode(CountryName.JPN.toCode());
        }
        else {
            throw  new Exception("Country not found");
        }
    }
}
