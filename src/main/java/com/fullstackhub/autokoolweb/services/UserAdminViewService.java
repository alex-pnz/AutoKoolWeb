package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.mappers.UserAdminViewMapper;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminViewService {

    private final UserAdminViewMapper userAdminViewMapper;
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    public UserAdminViewService(UserAdminViewMapper userAdminViewMapper,
                                UsersRepository usersRepository,
                                PasswordEncoder passwordEncoder) {
        this.userAdminViewMapper = userAdminViewMapper;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserAdminViewIn> getAllDto() {
        return usersRepository.findAll().stream().map(userAdminViewMapper::toDto).toList();
    }

    public User saveUserToDataBase(User user) {

        User tempUser = usersRepository.findByUsername(user.getUsername());

        if(tempUser == null || tempUser.getId().equals(user.getId())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return usersRepository.save(user);
        }

        return null;

    }

    public boolean deleteUserToDataBase(User user) {
        usersRepository.delete(user);
        return usersRepository.findById(user.getId()).isEmpty();
    }

}
