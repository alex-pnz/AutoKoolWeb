package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fullstackhub.autokoolweb.constants.StringConstants.LIMIT_USERS;

@Service
public class RegisterService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public int registerUser(String username, String password) {

        List<User> all = usersRepository.findAll();
        if(all.size()>LIMIT_USERS) {
            return 2;
        }

        User user = usersRepository.findByUsername(username);
        if (user != null) {
            return 1;
        }
        user = new User(username, passwordEncoder.encode(password), User.Role.USER);
        usersRepository.save(user);
        return 0;
    }
}
