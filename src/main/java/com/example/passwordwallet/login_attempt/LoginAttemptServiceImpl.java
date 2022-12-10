package com.example.passwordwallet.login_attempt;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    public void save(LoginAttempt loginAttempt) {
        loginAttemptRepository.save(loginAttempt);
    }
}
