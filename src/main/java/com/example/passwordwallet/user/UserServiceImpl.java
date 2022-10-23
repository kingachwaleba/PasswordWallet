package com.example.passwordwallet.user;

import com.example.passwordwallet.config.EnvConfig;
import com.example.passwordwallet.config.ErrorMessage;
import com.example.passwordwallet.encoders.HMACPasswordEncoder;
import com.example.passwordwallet.encoders.Sha512PasswordEncoder;
import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.password.PasswordService;
import com.example.passwordwallet.utils.SecureUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ErrorMessage errorMessage;
    private final PasswordService passwordService;
    private static final String pepper = EnvConfig.getPepper();

    public UserServiceImpl(UserRepository userRepository, ErrorMessage errorMessage,
                           @Lazy PasswordService passwordService) {
        this.userRepository = userRepository;
        this.errorMessage = errorMessage;
        this.passwordService = passwordService;
    }

    @Override
    public User saveUsingSHA512(User user) {
        try {
            String salt = SecureUtils.getSalt();
            user.setPassword(
                    "{sha512}" + Sha512PasswordEncoder.getPasswordWithSHA512(pepper + salt + user.getPassword()));
            user.setSalt(salt);
            user.setIsPasswordKeptAsHash(true);
            userRepository.save(user);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }

        return user;
    }

    @Override
    public User saveUsingHMAC(User user) {
        user.setPassword("{hmac}" + HMACPasswordEncoder.getPasswordWithHMAC(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        return userRepository.findByLogin(currentUserLogin);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }

    @Override
    public List<String> passwordValidation(String password) {
        List<String> messages = new ArrayList<>();

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            messages.add(errorMessage.get("user.password.regexp"));

        if (password.length() < 5 || password.length() > 50)
            messages.add(errorMessage.get("user.password.size"));

        return messages;
    }

    @Override
    public List<String> validation(BindingResult bindingResult, String password) {
        List<String> messages = getErrorList(bindingResult);
        messages.addAll(passwordValidation(password));

        return messages;
    }

    @Override
    public Boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User changeUserPassword(User user, Boolean isPasswordKeptAsHash, String newPassword) throws Exception {
        List<Password> passwordList = passwordService.getAll();
        for (Password password : passwordList)
            password.setPassword(passwordService.getOne(password.getId()));

        user.setPassword(newPassword);
        user.setIsPasswordKeptAsHash(isPasswordKeptAsHash);
        if (user.getIsPasswordKeptAsHash())
            user = saveUsingSHA512(user);
        else
            user = saveUsingHMAC(user);

        for (Password password : passwordList)
            passwordService.save(password);

        return user;
    }
}
