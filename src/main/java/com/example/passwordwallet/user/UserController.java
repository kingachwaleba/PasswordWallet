package com.example.passwordwallet.user;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody User user) {
//        return ResponseEntity.ok(userService.saveUsingSHA512(user));
        return ResponseEntity.ok(userService.saveUsingHMAC(user));
    }
}
