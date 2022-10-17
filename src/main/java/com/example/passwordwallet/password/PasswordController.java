package com.example.passwordwallet.password;

import com.example.passwordwallet.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class PasswordController {

    private final PasswordService passwordService;
    private final AuthenticationService authenticationService;

    public PasswordController(PasswordService passwordService, AuthenticationService authenticationService) {
        this.passwordService = passwordService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    @PostMapping("/add-password")
    public ResponseEntity<?> addPassword(@Valid @RequestBody Password password) throws Exception {
        return new ResponseEntity<>(passwordService.save(password), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPasswords() throws Exception {
        return new ResponseEntity<>(passwordService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/password/{id}")
    @PreAuthorize("@authenticationService.isPasswordOwner(#id)")
    public ResponseEntity<?> getDecryptedPassword(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(passwordService.getOne(Integer.parseInt(id)), HttpStatus.OK);
    }
}
