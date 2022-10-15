package com.example.passwordwallet.password;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Transactional
    @PostMapping("/add-password")
    public ResponseEntity<?> addPassword(@Valid @RequestBody Password password) throws Exception {
        return new ResponseEntity<>(passwordService.save(password), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() throws Exception {
        return new ResponseEntity<>(passwordService.getAll(), HttpStatus.OK);
    }
}
