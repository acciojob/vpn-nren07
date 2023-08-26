package com.driver.services.impl;
import com.driver.model.*;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        user=userRepository3.save(user);
        Integer countryId=countryRepository3.findByName(countryName);
        if(countryId==null) throw new Exception("Country not found");
        Country country=countryRepository3.findById(countryId).get();

        user.setOriginalIp(country.getCode()+user.getId());
        country.setUser(user);
        userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        return null;
    }
}
