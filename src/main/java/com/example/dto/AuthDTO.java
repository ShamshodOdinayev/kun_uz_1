package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthDTO {
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Name field must has a value")
    private String password;
}
