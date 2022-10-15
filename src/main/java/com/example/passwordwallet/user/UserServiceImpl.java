package com.example.passwordwallet.user;

import com.example.passwordwallet.config.EnvConfig;
import com.example.passwordwallet.encoders.HMACPasswordEncoder;
import com.example.passwordwallet.encoders.Sha512PasswordEncoder;
import com.example.passwordwallet.utils.SecureUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String pepper = EnvConfig.getPepper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
