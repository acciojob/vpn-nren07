package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User  connect(int userId, String countryName) throws Exception {
        if(!userRepository2.existsById(userId)) throw new Exception("User id is not present in db");
        User user = userRepository2.findById(userId).get();
        if (user.getConnected()) throw new Exception("Already connected");

        Country givenCountry = new Country();
        givenCountry.enrich(countryName);

        if (user.getOriginalCountry().getCountryName().equals(givenCountry.getCountryName())) {
            return user;
        }

        List<ServiceProvider> serviceProviders = user.getServiceProviderList();
        Integer lowestId = Integer.MAX_VALUE ;

        for (ServiceProvider provider : serviceProviders) {
            for (Country country : provider.getCountryList()) {
                if (country.getCode().equals(givenCountry.getCode()) || lowestId > provider.getId()) {
                    lowestId = provider.getId();
                }
            }
        }
        if(lowestId==Integer.MAX_VALUE) throw new Exception("Unable to connect");
        ServiceProvider serviceProviderWIthLowestId=serviceProviderRepository2.findById(lowestId).get();

        Connection connection = new Connection();
        connection.setUser(user);
        user.setConnected(true);

        //fk set
        user.getConnectionList().add(connection);

        user.setMaskedIp(new String(givenCountry.getCode() + "." + serviceProviderWIthLowestId.getId() + "." + user.getId()));
        connection.setServiceProvider(serviceProviderWIthLowestId);

        serviceProviderWIthLowestId.getConnectionList().add(connection);

        userRepository2.save(user);
        serviceProviderRepository2.save(serviceProviderWIthLowestId);

        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception{
        if(!userRepository2.existsById(userId)) throw new Exception("User id is not present in db");
        User user =userRepository2.findById(userId).get();

        if(!user.getConnected()){
            throw new Exception("Already Disconnected");
        }
        user.setConnected(false);
        user.setMaskedIp(null);
        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception{

        User receiver = userRepository2.findById(receiverId).get();
        CountryName receiverCountryName = null;

        if(receiver.getConnected()){
            String maskedCode=receiver.getMaskedIp().substring(0,3);
            if(maskedCode.equals("001")) receiverCountryName=CountryName.IND;
            else if(maskedCode.equals("002")) receiverCountryName=CountryName.USA;
            else if(maskedCode.equals("003")) receiverCountryName=CountryName.AUS;
            else if(maskedCode.equals("004")) receiverCountryName=CountryName.CHI;
            else if(maskedCode.equals("005")) receiverCountryName=CountryName.JPN;
            else{
                receiverCountryName=receiver.getOriginalCountry().getCountryName();
            }
        }

        User user =null;
        try{
            user=connect(senderId,receiverCountryName.toString());
        }catch (Exception e){
            throw new Exception("Cannot establish connecion");
        }
        return user;
    }
}
