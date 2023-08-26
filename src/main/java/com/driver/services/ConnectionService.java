package com.driver.services;

import com.driver.model.User;

public interface ConnectionService {

    public User connect(int userId, String countryName);

    public User disconnect(int userId);

    public User communicate(int senderId, int receiverId);
}
