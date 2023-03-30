package com.quinterodaniel.webcreditjava.repository;

import com.quinterodaniel.webcreditjava.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
