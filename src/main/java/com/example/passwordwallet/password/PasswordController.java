package com.example.passwordwallet.password;

import com.example.passwordwallet.config.ErrorMessage;
import com.example.passwordwallet.helpers.UpdateNotMasterPasswordHolder;
import com.example.passwordwallet.security.AuthenticationService;
import com.example.passwordwallet.shared_password.SharedPassword;
import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class PasswordController {

    private final PasswordService passwordService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ErrorMessage errorMessage;

    public PasswordController(PasswordService passwordService, UserService userService,
                              AuthenticationService authenticationService, ErrorMessage errorMessage) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.errorMessage = errorMessage;
    }

    @Transactional
    @PostMapping("/add-password")
    public ResponseEntity<?> addPassword(@Valid @RequestBody Password password, BindingResult bindingResult)
            throws Exception {
        if (passwordService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(passwordService.save(password), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/share-password/{userId}/{passwordId}")
    @PreAuthorize("@authenticationService.isPasswordOwner(#passwordId)")
    public ResponseEntity<?> sharePassword(@PathVariable int userId, @PathVariable int passwordId) {
        SharedPassword sharedPassword = passwordService.sharePassword(userId, passwordId);

        if (sharedPassword == null)
            return new ResponseEntity<>("You've already shared this password to this user!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(sharedPassword, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPasswords() throws Exception {
        return new ResponseEntity<>(passwordService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/password/{id}")
    @PreAuthorize("@authenticationService.isPasswordOwnerOrPassword(#id)")
    public ResponseEntity<?> getDecryptedPassword(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(passwordService.getOne(Integer.parseInt(id)), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/password/{id}")
    @PreAuthorize("@authenticationService.isPasswordOwner(#id)")
    public ResponseEntity<?> deletePassword(@PathVariable int id) {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        if (userService.ifInReadMode(currentLoggedInUser))
            return new ResponseEntity<>(
                    "You have to switch to modify mode to delete password!", HttpStatus.UNAUTHORIZED);

        passwordService.delete(id);

        return new ResponseEntity<>("Successfully deleted password!", HttpStatus.OK);
    }

    @PutMapping("/password/{id}")
    @PreAuthorize("@authenticationService.isPasswordOwner(#id)")
    public ResponseEntity<?> editPassword(@Valid @RequestBody UpdateNotMasterPasswordHolder newPassword,
                                          BindingResult bindingResult, @PathVariable int id) throws Exception {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        if (userService.ifInReadMode(currentLoggedInUser))
            return new ResponseEntity<>(
                    "You have to switch to modify mode to delete password!", HttpStatus.UNAUTHORIZED);

        if (passwordService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(passwordService.edit(
                passwordService.getOneById(id).orElseThrow(PasswordNotFoundException::new), newPassword),
                HttpStatus.OK);
    }
}
