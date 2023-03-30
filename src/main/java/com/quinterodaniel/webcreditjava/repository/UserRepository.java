package com.quinterodaniel.webcreditjava.repository;

import com.quinterodaniel.webcreditjava.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    List<User> findAll();
}
