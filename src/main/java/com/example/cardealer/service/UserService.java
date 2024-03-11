package com.example.cardealer.service;

import com.example.cardealer.entity.Code;
import com.example.cardealer.entity.auth.*;
import com.example.cardealer.exceptions.auth.DealerExistingWithNIP;
import com.example.cardealer.exceptions.auth.UserExistingWithMail;
import com.example.cardealer.exceptions.auth.UserExistingWithName;
import com.example.cardealer.repository.DealerRepository;
import com.example.cardealer.repository.UserRepository;
import com.example.cardealer.translator.DealerDtoToDealer;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DealerRepository dealerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DealerDtoToDealer dealerDtoToDealer;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    public ResponseEntity<AuthResponse> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByLoginAndLockAndEnabled(authRequest.getLogin()).orElse(null);
        if (user != null) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie("refresh", generateToken(authRequest.getLogin(), refreshExp), refreshExp, response);
                Cookie cookie = cookieService.generateCookie("Authorization", generateToken(authRequest.getLogin(), exp), exp, response);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
            } else {
                return ResponseEntity.status(404).body(new AuthResponse(Code.A1));
            }
        }
        return ResponseEntity.status(404).body(new AuthResponse(Code.A2));
    }

    public ResponseEntity<AuthResponse> register(UserRegisterDTO userRegisterDTO) {

        if (userRepository.findUserByLogin(userRegisterDTO.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A4));
        }

        if (userRepository.findUserByEmail(userRegisterDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A5));
        }

        dealerRepository.findDealerById(userRegisterDTO.getDealer()).ifPresent(value -> {
            User user = new User();
            user.setLock(false);
            user.setEnabled(true);
            user.setUuid(UUID.randomUUID().toString());
            user.setLogin(userRegisterDTO.getLogin());
            user.setPassword(userRegisterDTO.getPassword());
            user.setEmail(userRegisterDTO.getEmail());
            user.setRole(Role.USER);
            user.setDealer(value);

            saveUser(user);
        });

        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));

    }

    public ResponseEntity<AuthResponse> logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = cookieService.removeCookie(request.getCookies(),"Authorization", response);
        if (cookie != null) {
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(request.getCookies(),"refresh", response);
        if (cookie != null) {
            response.addCookie(cookie);
        }
        return  ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    public void validateToken(HttpServletRequest request,HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException{
        String token = null;
        String refresh = null;
        if (request.getCookies() != null){
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
        }else {
            throw new IllegalArgumentException("Token can't be null");
        }
        try {
            jwtService.validateToken(token);
        }catch (IllegalArgumentException | ExpiredJwtException e){
            jwtService.validateToken(refresh);
            Cookie refreshCokkie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh,refreshExp), refreshExp, response);
            Cookie cookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh,exp), exp, response);
            response.addCookie(cookie);
            response.addCookie(refreshCokkie);
        }
    }

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    private String generateToken(String username,int exp) {
        return jwtService.generateToken(username,exp);
    }

    public void registerDealer(DealerDTO dealerDTO) {
        dealerRepository.findDealerByNip(dealerDTO.getNip()).ifPresent(value->{
            throw new DealerExistingWithNIP("Dealer already exist with this NIP");
        });

        Dealer dealer =  dealerDtoToDealer.toDealer(dealerDTO);
        dealer.setUuid(UUID.randomUUID().toString());

        dealerRepository.save(dealer);
    }

    public ResponseEntity<?> editDealer(Dealer dealer) {
        Optional<Dealer> existingDealerOpt = dealerRepository.findDealerByUuid(dealer.getUuid());

        if (existingDealerOpt.isPresent()) {
            Dealer existingDealer = existingDealerOpt.get();
            BeanUtils.copyProperties(dealer, existingDealer);
            dealerRepository.save(existingDealer);
            return ResponseEntity.ok(existingDealer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono dealera");
        }
    }
}
