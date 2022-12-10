package com.example.passwordwallet.user;

import com.example.passwordwallet.config.EnvConfig;
import com.example.passwordwallet.config.ErrorMessage;
import com.example.passwordwallet.config.JwtProvider;
import com.example.passwordwallet.config.JwtResponse;
import com.example.passwordwallet.helpers.LoginForm;
import com.example.passwordwallet.helpers.UpdatePasswordHolder;
import com.example.passwordwallet.ip_address.IpAddress;
import com.example.passwordwallet.ip_address.IpAddressService;
import com.example.passwordwallet.login_attempt.LoginAttempt;
import com.example.passwordwallet.login_attempt.LoginAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final IpAddressService ipAddressService;
    private final LoginAttemptService loginAttemptService;
    private final ErrorMessage errorMessage;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private static final String pepper = EnvConfig.getPepper();

    public UserController(UserService userService, IpAddressService ipAddressService,
                          LoginAttemptService loginAttemptService, ErrorMessage errorMessage,
                          AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.ipAddressService = ipAddressService;
        this.loginAttemptService = loginAttemptService;
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

    @Transactional
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

        String ipAddressValue2 = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

        if (ipAddressService.findOneByIpAddress(ipAddressValue2).isPresent()) {
            IpAddress ipAddress2 = ipAddressService.findOneByIpAddress(ipAddressValue2).get();
            if (ipAddress2.getIsPermLock())
                return new ResponseEntity<>("TO IP JEST ZABLOKOWANE -> ZGLOS SIE ZEBY CI ODBLOKOWAC", HttpStatus.UNAUTHORIZED);
            if (!(ipAddress2.getTempLockTime() == null) && ipAddress2.getTempLockTime().isAfter(LocalDateTime.now()))
                return new ResponseEntity<>("TO IP JEST POD KWARANTANNA -> POCZEKAJ", HttpStatus.UNAUTHORIZED);
        }

        if (!(user.getLockoutTime() == null) && user.getLockoutTime().isAfter(LocalDateTime.now()))
            return new ResponseEntity<>("JESTES POD KWARANTANNA -> POCZEKAJ", HttpStatus.UNAUTHORIZED);

        try {
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
        catch (AuthenticationException authenticationException) {
            String ipAddressValue = ((ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

            IpAddress ipAddress;
            if (ipAddressService.findOneByIpAddress(ipAddressValue).isPresent()) {
                ipAddress = ipAddressService.findOneByIpAddress(ipAddressValue).get();
            }
            else {
                ipAddress = new IpAddress();
                ipAddress.setIpAddress(ipAddressValue);
            }
            ipAddress.setUnsuccessfulLoginCount(ipAddress.getUnsuccessfulLoginCount() + 1);
            ipAddress.setIsPermLock(ipAddress.getUnsuccessfulLoginCount() >= 10);

            LoginAttempt loginAttempt = new LoginAttempt();
            loginAttempt.setAttemptTime(LocalDateTime.now());
            loginAttempt.setUser(user);
            loginAttempt.setIsCorrect(false);
            ipAddress.addLoginAttempt(loginAttempt);

            user.setUnsuccessfulLoginCount(user.getUnsuccessfulLoginCount() + 1);

            switch (user.getUnsuccessfulLoginCount()) {
                case 2:
                    user.setLockoutTime(LocalDateTime.now().plusSeconds(5));
                    break;
                case 3:
                    user.setLockoutTime(LocalDateTime.now().plusSeconds(10));
                    break;
                case 4:
                    user.setLockoutTime(LocalDateTime.now().plusSeconds(60));
                    break;
                default:
                    user.setLockoutTime(null);
                    break;
            }

            switch (ipAddress.getUnsuccessfulLoginCount()) {
                case 2:
                    ipAddress.setTempLockTime(LocalDateTime.now().plusSeconds(5));
                    break;
                case 3:
                    ipAddress.setTempLockTime(LocalDateTime.now().plusSeconds(10));
                    break;
            }

            ipAddressService.save(ipAddress);
            loginAttemptService.save(loginAttempt);
            userService.save(user);

            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordHolder updatePasswordHolder,
                                            BindingResult bindingResult) throws Exception {
        String password = updatePasswordHolder.getPassword();
        Boolean isPasswordKeptAsHash = updatePasswordHolder.getIsPasswordKeptAsHash();
        if (userService.validation(bindingResult, password).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return new ResponseEntity<>(
                userService.changeUserPassword(user, isPasswordKeptAsHash, password), HttpStatus.OK);
    }
}
