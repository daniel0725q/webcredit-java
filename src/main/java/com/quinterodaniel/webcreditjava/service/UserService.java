package com.quinterodaniel.webcreditjava.service;

import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import com.quinterodaniel.webcreditjava.dto.UserDto;
import com.quinterodaniel.webcreditjava.entity.Simulation;
import com.quinterodaniel.webcreditjava.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
