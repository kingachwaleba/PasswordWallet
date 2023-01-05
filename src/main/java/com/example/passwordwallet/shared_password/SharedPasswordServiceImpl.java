package com.example.passwordwallet.shared_password;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedPasswordServiceImpl implements SharedPasswordService {

    private final SharedPasswordRepository sharedPasswordRepository;

    public SharedPasswordServiceImpl(SharedPasswordRepository sharedPasswordRepository) {
        this.sharedPasswordRepository = sharedPasswordRepository;
    }

    @Override
    public SharedPassword save(User user, User owner, Password password) {
        SharedPassword sharedPassword = new SharedPassword();

        sharedPassword.setUser(user);
        sharedPassword.setOwner(owner);
        sharedPassword.setPassword(password);

        user.addSharedPassword(sharedPassword);
        password.addSharedPassword(sharedPassword);

        return sharedPasswordRepository.save(sharedPassword);
    }

    @Override
    public List<SharedPassword> findAllByOwnerAndUserAndPassword(User owner, User user, Password password) {
        return sharedPasswordRepository.findAllByOwnerAndUserAndPassword(owner, user, password);
    }
}
