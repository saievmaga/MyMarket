package com.saiev.MyMarket.repositories;

import com.saiev.MyMarket.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName(String userName);
}
