package com.saiev.MyMarket.repositories;

import com.saiev.MyMarket.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findOneByName(String theRoleName);
}
