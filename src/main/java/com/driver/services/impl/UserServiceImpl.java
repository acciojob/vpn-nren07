package com.driver.services.impl;
import com.driver.model.*;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception {
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        Country country=new Country();
        try{
            country.enrich(countryName);
        }catch(Exception e){
            throw new Exception("Country not found");
        }
        //set fk variable
        country.setUser(user);
        user.setOriginalCountry(country);

        user=userRepository3.save(user); //this is for user id assign
        user.setOriginalIp(new String(user.getOriginalCountry().getCode()+"."+user.getId()));

        //bidirectional mapping here
        return userRepository3.save(user);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        //all validation check

        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        List<ServiceProvider>serviceProviderList=user.getServiceProviderList();
        List<User>userList=serviceProvider.getUsers();

        serviceProviderList.add(serviceProvider);
        userList.add(user);
        //fk set
        user.setServiceProviderList(serviceProviderList); // user fk set

        //bidirectional mapping
        serviceProviderRepository3.save(serviceProvider);
        serviceProvider.setUsers(userList); //service provider fk set
        //bidirectional saving
        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
