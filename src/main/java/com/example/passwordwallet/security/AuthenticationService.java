package com.example.passwordwallet.security;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.password.PasswordNotFoundException;
import com.example.passwordwallet.password.PasswordService;
import com.example.passwordwallet.shared_password.SharedPassword;
import com.example.passwordwallet.shared_password.SharedPasswordService;
import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordService passwordService;
    private final SharedPasswordService sharedPasswordService;

    public AuthenticationService(UserService userService, PasswordService passwordService,
                                 SharedPasswordService sharedPasswordService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.sharedPasswordService = sharedPasswordService;
    }

    public boolean isPasswordOwner(int passwordId) {
        Password password = passwordService.getOneById(passwordId).orElseThrow(PasswordNotFoundException::new);
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return password.getUser().equals(currentLoggedInUser);
    }

    public boolean isPasswordOwnerOrPassword(int passwordId) {
        if (isPasswordOwner(passwordId))
            return true;

        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        Password password = passwordService.getOneById(passwordId).orElseThrow(PasswordNotFoundException::new);
        List<SharedPassword> sharedPasswordList = sharedPasswordService.
                findAllByUserAndPassword(currentLoggedInUser, password);

        return sharedPasswordList.size() != 0;
    }
}
