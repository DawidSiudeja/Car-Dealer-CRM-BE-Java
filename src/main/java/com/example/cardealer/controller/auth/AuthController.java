package com.example.cardealer.controller.auth;

import com.example.cardealer.entity.Code;
import com.example.cardealer.entity.auth.*;
import com.example.cardealer.exceptions.auth.DealerExistingWithNIP;
import com.example.cardealer.exceptions.auth.UserExistingWithMail;
import com.example.cardealer.exceptions.auth.UserExistingWithName;
import com.example.cardealer.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @RequestMapping(path = "/dealer/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> addNewDealer(@Valid @RequestBody DealerDTO dealerDTO){
        try {
            userService.registerDealer(dealerDTO);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (DealerExistingWithNIP existing) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A6));
        }
    }

    @RequestMapping(path = "/dealer/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> editDealer(@RequestBody Dealer dealer) {
        return userService.editDealer(dealer);
    }

    @RequestMapping(path = "/user/register",method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> addNewUser(@Valid @RequestBody UserRegisterDTO user){
        return userService.register(user);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@RequestBody User user, HttpServletResponse response){
        return userService.login(response,user);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response, HttpServletRequest request){
        return userService.logout(request, response);
    }

    @RequestMapping(path = "/validate", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> validateToken(HttpServletRequest request,HttpServletResponse response) {
        try{
            userService.validateToken(request,response);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        }catch (IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(401).body(new AuthResponse(Code.A3));
        }
    }

}
