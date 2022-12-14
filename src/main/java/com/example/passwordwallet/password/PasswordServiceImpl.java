package com.example.passwordwallet.password;

import com.example.passwordwallet.user.User;
import com.example.passwordwallet.user.UserNotFoundException;
import com.example.passwordwallet.user.UserService;
import com.example.passwordwallet.utils.SecureUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<Password> getAll() {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return passwordRepository.findAllByUser(currentLoggedInUser);
    }

    @Override
    public String getOne(int id) throws Exception {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        Password password = passwordRepository.findById(id).orElseThrow(PasswordNotFoundException::new);

        return SecureUtils.decrypt(password.getPassword(), SecureUtils.generateKey(currentLoggedInUser.getPassword()));
    }

    @Override
    public Optional<Password> getOneById(int id) {
        return passwordRepository.findById(id);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
}
