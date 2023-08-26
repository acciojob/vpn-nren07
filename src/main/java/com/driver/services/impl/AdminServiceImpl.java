package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.google.common.base.Enums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.lang.Character.toUpperCase;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin=new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin= adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) throws Exception {
        if(!adminRepository1.existsById(adminId)) throw new Exception("admin is not valid");
        Admin admin=adminRepository1.findById(adminId).get();
        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.setName(providerName);
        //set fk valiables
        serviceProvider.setAdmin(admin);
        serviceProvider=serviceProviderRepository1.save(serviceProvider);
        //bidirectional mapping
        admin.getServiceProviders().add(serviceProvider);
        return adminRepository1.save(admin);
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {
//        add a country under the serviceProvider and return respective service provider
//        country name would be a 3-character string out of ind, aus, usa, chi, jpn. Each character can be in uppercase or lowercase. You should create a new Country object based on the given country name and add it to the country list of the service provider. Note that the user attribute of the country in this case would be null.
//        In case country name is not amongst the above mentioned strings, throw "Country not found" exception

        if(!serviceProviderRepository1.existsById(serviceProviderId)) throw new Exception("Service provider is not valid");
        if(!Enums.getIfPresent(CountryName.class, countryName.toUpperCase()).isPresent()) {
            throw new Exception("Country not found");
        }
        Country country=new Country();
        country.setCountryName(CountryName.valueOf(countryName.toUpperCase()));

        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();
        country.setServiceProvider(serviceProvider);
        Country countryById=countryRepository1.save(country);
        serviceProvider.getCountryList().add(countryById);
        return serviceProviderRepository1.save(serviceProvider);

    }
}
