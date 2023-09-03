package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) throws Exception {
        if(!adminRepository1.existsById(adminId)) throw new Exception("admin not present in db");
        Admin admin=adminRepository1.findById(adminId).get();
        ServiceProvider serviceProvider=new ServiceProvider();

        serviceProvider.setName(providerName);
        //set fk valiables
        serviceProvider.setAdmin(admin);
        admin.getServiceProviders().add(serviceProvider);

        //bidirectional mapping
        return adminRepository1.save(admin);
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {
        if(!serviceProviderRepository1.existsById(serviceProviderId)) throw new Exception("service provider id is not present in db");
        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();
        Country country=new Country();
        country.enrich(countryName);
        //set fk variable
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);

        //bi_directional mapping
        serviceProvider=serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
