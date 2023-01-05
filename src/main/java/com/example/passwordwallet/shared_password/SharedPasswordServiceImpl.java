package com.example.passwordwallet.shared_password;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<SharedPassword> getOneById(int id) {
        return sharedPasswordRepository.findById(id);
    }

    @Override
    public List<SharedPassword> findAllByUserAndPassword(User user, Password password) {
        return sharedPasswordRepository.findAllByUserAndPassword(user, password);
    }

    @Override
    public List<SharedPassword> findAllByOwnerAndUserAndPassword(User owner, User user, Password password) {
        return sharedPasswordRepository.findAllByOwnerAndUserAndPassword(owner, user, password);
    }

    @Override
    public List<SharedPassword> findAllByUser(User user) {

        return sharedPasswordRepository.findAllByUser(user);
    }
}
