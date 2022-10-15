package com.example.passwordwallet.password;

import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import com.example.passwordwallet.utils.SecureUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final UserService userService;

    public PasswordServiceImpl(PasswordRepository passwordRepository, UserService userService) {
        this.passwordRepository = passwordRepository;
        this.userService = userService;
    }

    @Override
    public Password save(Password password) throws Exception {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        password.setUser(currentLoggedInUser);
        password.setPassword(SecureUtils.encrypt(password.getPassword(),
                SecureUtils.generateKey(currentLoggedInUser.getPassword())));
        currentLoggedInUser.addPassword(password);
        passwordRepository.save(password);

        return password;
    }

    @Override
    public List<String> getAll() throws Exception {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        List<Password> passwordList = passwordRepository.findAllByUser(currentLoggedInUser);
        List<String> newPasswordList = new ArrayList<>();
        for (Password password : passwordList) {
            newPasswordList.add(SecureUtils.decrypt(password.getPassword(),
                    SecureUtils.generateKey(currentLoggedInUser.getPassword())));
        }

        return newPasswordList;
    }
}
