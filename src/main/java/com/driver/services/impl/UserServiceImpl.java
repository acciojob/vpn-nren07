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
        country.enrich(countryName);
        //set fk variable
        country.setUser(user);
        user.setOriginalCountry(country);

        user=userRepository3.save(user); //this is for user id assign
        user.setOriginalIp(new String(user.getOriginalCountry().getCode()+"."+user.getId()));

        //bidirectional mapping here
        userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) throws Exception {
        if(!userRepository3.existsById(userId)) throw new Exception("user not found");
        if(!serviceProviderRepository3.existsById(serviceProviderId)) throw new Exception("serviceProvider is invalid");
        //all validation check
        //connect fk
        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        List<ServiceProvider>serviceProviderList=user.getServiceProviderList();
        List<User>userList=serviceProvider.getUsers();
        //fk set




        //bidirectional mapping
        serviceProvider=serviceProviderRepository3.save(serviceProvider);
        serviceProviderList.add(serviceProvider);
        user.setServiceProviderList(serviceProviderList); // user fk set

        userList.add(user);
        serviceProvider.setUsers(userList); //service provider fk set
        userRepository3.save(user);
        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
}
