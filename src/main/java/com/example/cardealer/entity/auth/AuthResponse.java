package com.example.cardealer.entity.auth;

import com.example.cardealer.entity.Code;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AuthResponse {
    private final String timestamp;
    private final String message;
    private final Code code;

    public AuthResponse(Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.label;
        this.code = code;
    }

    public AuthResponse() {
        this.timestamp = null;
        this.message = null;
        this.code = null;
    }
}