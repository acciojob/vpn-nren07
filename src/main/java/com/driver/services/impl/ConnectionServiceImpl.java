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
    public User connect(int userId, String countryName) throws Exception {
        if(!userRepository2.existsById(userId)) throw new Exception("User is not found");
        User user = userRepository2.findById(userId).get();
        if (user.getConnected()) throw new Exception("Already connected");

        Country givenCountry = new Country();
        givenCountry.enrich(countryName);

        if (user.getOriginalCountry().getCountryName().equals(givenCountry.getCountryName())) {
            return user;
        }

        List<ServiceProvider> serviceProviders = user.getServiceProviderList();
        ServiceProvider serviceProviderWIthLowestId = null;
        Integer lowestId = -1;

        for (ServiceProvider provider : serviceProviders) {
            for (Country country : provider.getCountryList()) {
                if (country.getCode().equals(givenCountry.getCode()) || lowestId > provider.getId()) {
                    serviceProviderWIthLowestId = provider;
                    lowestId = serviceProviderWIthLowestId.getId();
                }
            }
        }
        //         where the maskedIp is "updatedCountryCode.serviceProviderId.userId"
        //         and return the updated user. If multiple service providers
        //        allow you to connect to the country, use the service provider having smallest id.

        if (serviceProviderWIthLowestId == null) throw new Exception("Unable to connect");

        Connection connection = new Connection();
        connection.setUser(user);
        user.setConnected(true);

        //fk set
        List<Connection> connections = user.getConnectionList();
        connections.add(connection);
        user.setConnectionList(connections);

        user.setMaskedIp(new String(givenCountry.getCode() + "." + serviceProviderWIthLowestId.getId() + "." + user.getId()));
        connection.setServiceProvider(serviceProviderWIthLowestId);

        List<Connection> connectionList = serviceProviderWIthLowestId.getConnectionList();
        connectionList.add(connection);
        serviceProviderWIthLowestId.setConnectionList(connectionList);

        userRepository2.save(user);
        serviceProviderRepository2.save(serviceProviderWIthLowestId);
        connectionRepository2.save(connection);

        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception{ //

        if(!userRepository2.existsById(userId)) throw new Exception("user is not present in DB");

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

        //Establish a connection between sender and receiver users
        //To communicate to the receiver, sender should be in the current country of the receiver.
        //If the receiver is connected to a vpn, his current country is the one he is connected to.
        //If the receiver is not connected to vpn, his current country is his original country.
        //The sender is initially not connected to any vpn. If the sender's original country does not match receiver's current country, we need to connect the sender to a suitable vpn. If there are multiple options, connect using the service provider having smallest id
        //If the sender's original country matches receiver's current country, we do not need to do anything as they can communicate. Return the sender as it is.
        //If communication can not be established due to any reason, throw "Cannot establish communication" exception
        if(!userRepository2.existsById(senderId) || !userRepository2.existsById(receiverId)) throw new Exception("given id not present in db");
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
