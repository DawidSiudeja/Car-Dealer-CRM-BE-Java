package com.example.cardealer.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public Cookie generateCookie(String name,String value,int exp, HttpServletResponse response){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(exp);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.setHeader("Set-Cookie", String.format("%s=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=Lax", name, value, exp));
        return cookie;
    }

    public Cookie removeCookie(Cookie[] cookies, String name, HttpServletResponse response){
        for (Cookie cookie:cookies){
            if (cookie.getName().equals(name)){
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.setHeader("Set-Cookie", String.format("%s=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=Lax", name));

                return cookie;
            }
        }
        return null;
    }

}
