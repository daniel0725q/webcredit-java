package com.quinterodaniel.webcreditjava.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;
}
