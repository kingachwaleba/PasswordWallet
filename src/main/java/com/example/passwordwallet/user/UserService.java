package com.example.passwordwallet.user;

public interface UserService {

    User saveUsingSHA512(User user);
    User saveUsingHMAC(User user);
}
