package com.example.demo.util;

import jakarta.validation.*;

public class ValidationUtils {

    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new ValidationException("Password must be at least 6 characters long");
        }
    }
}
