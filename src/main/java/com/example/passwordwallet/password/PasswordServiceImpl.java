package com.example.passwordwallet.password;

import com.example.passwordwallet.shared_password.SharedPassword;
import com.example.passwordwallet.shared_password.SharedPasswordService;
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
    private final SharedPasswordService sharedPasswordService;

    public PasswordServiceImpl(PasswordRepository passwordRepository, UserService userService,
                               SharedPasswordService sharedPasswordService) {
        this.passwordRepository = passwordRepository;
        this.userService = userService;
        this.sharedPasswordService = sharedPasswordService;
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
    public Optional<Password> findById(int id) {
        return passwordRepository.findById(id);
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

    @Override
    public SharedPassword sharePassword(int userId, int passwordId) {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        Password password = findById(passwordId).orElseThrow(PasswordNotFoundException::new);

        if (sharedPasswordService.findAllByOwnerAndUserAndPassword(currentLoggedInUser, user, password).size() != 0)
            return null;

        return sharedPasswordService.save(user, currentLoggedInUser, password);
    }
}
