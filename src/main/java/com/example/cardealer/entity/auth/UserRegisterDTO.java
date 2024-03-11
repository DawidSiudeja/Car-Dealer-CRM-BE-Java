package com.example.cardealer.entity.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserRegisterDTO {

    @Length(min = 5,max = 50, message = "Login powinien mieć od 5 do 50 znaków")
    private String login;
    @Email
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 8,max = 75, message = "Hasło powinno skałdać się od 8 do 75 znaków")
    private String password;
    private Role role;
    private Integer dealer;
}
