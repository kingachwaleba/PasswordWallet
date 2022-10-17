package com.example.passwordwallet.security;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.password.PasswordNotFoundException;
import com.example.passwordwallet.password.PasswordService;
import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordService passwordService;

    public AuthenticationService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    public boolean isPasswordOwner(int passwordId) {
        Password password = passwordService.getOneById(passwordId).orElseThrow(PasswordNotFoundException::new);
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return password.getUser().equals(currentLoggedInUser);
    }
}
