package com.example.cardealer.exceptions.auth;

public class DealerExistingWithNIP extends RuntimeException{
    public DealerExistingWithNIP(String message) {
        super(message);
    }

    public DealerExistingWithNIP(String message, Throwable cause) {
        super(message, cause);
    }

    public DealerExistingWithNIP(Throwable cause) {
        super(cause);
    }
}
