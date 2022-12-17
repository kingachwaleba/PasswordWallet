package com.example.passwordwallet.login_attempt;

import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserService userService;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository, UserService userService) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.userService = userService;
    }

    @Override
    public void save(LoginAttempt loginAttempt) {
        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public List<LoginAttempt> findAllByIsCorrectAndUserOrderByAttemptTimeDesc(Boolean isCorrect, User user) {
        return loginAttemptRepository.findAllByIsCorrectAndUserOrderByAttemptTimeDesc(isCorrect, user);
    }

    public Map<String, LocalDateTime> getLastLoginAttempts() {
        Map<String, LocalDateTime> lastLoginAttemptsMap = new HashMap<>();
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        List<LoginAttempt> lastSuccessfulLoginAttempts = findAllByIsCorrectAndUserOrderByAttemptTimeDesc(
                true, currentLoggedInUser);
        if (lastSuccessfulLoginAttempts.size() > 1)
            lastLoginAttemptsMap.put("successful", lastSuccessfulLoginAttempts.get(1).getAttemptTime());
        else if (lastSuccessfulLoginAttempts.size() == 1)
            lastLoginAttemptsMap.put("successful", lastSuccessfulLoginAttempts.get(0).getAttemptTime());

        List<LoginAttempt> lastUnsuccessfulLoginAttempts = findAllByIsCorrectAndUserOrderByAttemptTimeDesc(
                false, currentLoggedInUser);
        if (lastUnsuccessfulLoginAttempts.size() == 0)
            lastLoginAttemptsMap.put("unsuccessful", null);
        else
            lastLoginAttemptsMap.put("unsuccessful", lastUnsuccessfulLoginAttempts.get(0).getAttemptTime());

        return lastLoginAttemptsMap;
    }
}
