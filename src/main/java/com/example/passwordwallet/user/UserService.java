package com.example.passwordwallet.user;

import java.util.Optional;

public interface UserService {

    User saveUsingSHA512(User user);
    User saveUsingHMAC(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findCurrentLoggedInUser();
}
