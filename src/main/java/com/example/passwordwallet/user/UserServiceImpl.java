package com.example.passwordwallet.user;

import com.example.passwordwallet.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${app.pepper}")
    private String pepper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User saveUsingSHA512(User user) {
        try {
            String salt = SecureUtils.getSalt();
            user.setPassword(SecureUtils.getPasswordWithSHA512(pepper + salt + user.getPassword()));
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
        try {
            String salt = SecureUtils.getSalt();
            user.setPassword(SecureUtils.getPasswordWithHMAC(user.getPassword(), salt));
            userRepository.save(user);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }

        return user;
    }
}
