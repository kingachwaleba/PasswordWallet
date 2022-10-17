package com.example.passwordwallet.user;

import com.example.passwordwallet.config.EnvConfig;
import com.example.passwordwallet.config.ErrorMessage;
import com.example.passwordwallet.config.JwtProvider;
import com.example.passwordwallet.config.JwtResponse;
import com.example.passwordwallet.helpers.LoginForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final ErrorMessage errorMessage;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private static final String pepper = EnvConfig.getPepper();

    public UserController(UserService userService, ErrorMessage errorMessage, AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider) {
        this.userService = userService;
        this.errorMessage = errorMessage;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (userService.validation(bindingResult, user.getPassword()).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        if (userService.existsByEmail(user.getEmail()) || userService.existsByLogin(user.getLogin()))
            return new ResponseEntity<>(errorMessage.get("register.takenCredentials"), HttpStatus.CONFLICT);

        if (user.getIsPasswordKeptAsHash())
            return ResponseEntity.ok(userService.saveUsingSHA512(user));
        else
            return ResponseEntity.ok(userService.saveUsingHMAC(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest, BindingResult bindingResult) {
        if (userService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(errorMessage.get("login.error"), HttpStatus.UNAUTHORIZED);

        User user = optionalUser.get();

        Authentication authentication;
        if (user.getIsPasswordKeptAsHash())
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, pepper + user.getSalt() + password));
        else
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date expiryDate = jwtProvider.getExpiryDateFromJwtToken(jwtToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwtToken, expiryDate, userDetails.getUsername()));
    }
}
