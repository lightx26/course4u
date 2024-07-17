package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.authdto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class SignUpValidator {

    public static List<String> validateSignUpRequest(SignUpRequest signUpRequest) {
        List<String> errors = new ArrayList<>();

        // Validate username
        if (!StringUtils.hasText(signUpRequest.getUsername())) {
            errors.add("Username is required.");
        } else {
            if (signUpRequest.getUsername().length() > 50) {
                errors.add("Max length for username is 50 characters.");
            }
            if (hasSpecialChar(signUpRequest.getUsername())) {
                errors.add("Username cannot have special characters.");
            }
        }

        // Validate email
        if (!StringUtils.hasText(signUpRequest.getEmail())) {
            errors.add("Email is required.");
        } else {
            if (!isValidEmail(signUpRequest.getEmail())) {
                errors.add("Please enter a valid email address.");
            }
        }

        // Validate password
        List<String> passwordErrors = validatePassword(signUpRequest.getPassword(), signUpRequest.getConfirmPassword());
        errors.addAll(passwordErrors);

        // Validate fullname
        if (signUpRequest.getFullname() != null && signUpRequest.getFullname().length() > 50) {
            errors.add("Max length for fullname is 50 characters.");
        }

        // Validate dateOfBirth
        if (signUpRequest.getDateofbirth() != null && !signUpRequest.getDateofbirth().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dateOfBirth = LocalDate.parse(signUpRequest.getDateofbirth(), formatter);
                if (dateOfBirth.isAfter(LocalDate.now())) {
                    errors.add("Date of birth cannot be a future date.");
                }
            } catch (DateTimeParseException e) {
                errors.add("Invalid date of birth format. Use MM/dd/yyyy.");
            }
        }

        return errors; // Return list of errors
    }

    private static boolean hasSpecialChar(String str) {
        return str.matches(".*[`~!@#$%^&*()\\-_=+{};:'\"\\\\|,.<>/? ].*");
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@mgm-tp\\.com$");
    }

    private static List<String> validatePassword(String password, String confirmPassword) {
        List<String> passwordErrors = new ArrayList<>();

        if (!StringUtils.hasText(password) || !StringUtils.hasText(confirmPassword)) {
            passwordErrors.add("Password and confirm password are required.");
        } else {
            if (password.length() < 8) {
                passwordErrors.add("Password must be at least 8 characters.");
            }
            if (!password.matches(".*[A-Z].*")) {
                passwordErrors.add("Password must contain at least one uppercase letter.");
            }
            if (!password.matches(".*\\d.*")) {
                passwordErrors.add("Password must contain at least one number.");
            }
            if (!password.matches(".*[`~!@#$%^&*()\\-_=+{};:'\"\\\\|,.<>/?]+.*")) {
                passwordErrors.add("Password must contain at least one special character.");
            }
            if (!password.equals(confirmPassword)) {
                passwordErrors.add("Password and confirm password must be the same.");
            }
        }

        return passwordErrors;
    }
}
