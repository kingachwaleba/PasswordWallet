package com.example.passwordwallet.login_attempt;

import com.example.passwordwallet.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface LoginAttemptService {

    void save(LoginAttempt loginAttempt);
    List<LoginAttempt> findAllByIsCorrectAndUserOrderByAttemptTimeDesc(Boolean isCorrect, User user);
    Map<String, LocalDateTime> getLastLoginAttempts();
}
