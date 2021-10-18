package com.saiev.MyMarket.services;

import com.saiev.MyMarket.entities.SystemUser;
import com.saiev.MyMarket.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
    boolean save(SystemUser systemUser);
}
