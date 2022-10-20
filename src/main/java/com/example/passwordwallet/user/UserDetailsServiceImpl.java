package com.example.passwordwallet.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Implement loadUserByUsername method but use email instead of username
    // Return email as a parameter and return the user
    // If there is no user with that email, it will throw UserNotFoundException
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findByEmail(s).orElseThrow(UserNotFoundException::new);

        return UserPrinciple.build(user);
    }
}
