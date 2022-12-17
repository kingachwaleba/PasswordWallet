package com.example.passwordwallet.login_attempt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;

    public LoginAttemptController(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @GetMapping("/attempts")
    public ResponseEntity<?> getAllAttempts()  {
        return new ResponseEntity<>(loginAttemptService.getLastLoginAttempts(), HttpStatus.OK);
    }
}
