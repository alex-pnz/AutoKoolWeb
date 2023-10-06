package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.mappers.UserAdminViewMapper;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminViewService {

    private final UserAdminViewMapper userAdminViewMapper;
    private final UsersRepository usersRepository;

    public UserAdminViewService(UserAdminViewMapper userAdminViewMapper, UsersRepository usersRepository) {
        this.userAdminViewMapper = userAdminViewMapper;
        this.usersRepository = usersRepository;
    }

    public List<UserAdminViewIn> getAllDto() {
        return usersRepository.findAll().stream().map(userAdminViewMapper::toDto).toList();
    }

    public User saveUserToDataBase(User user) {

        User tempUser = usersRepository.findByUsername(user.getUsername());

        if(tempUser == null || tempUser.getId().equals(user.getId())){
            return usersRepository.save(user);
        }

        return null;

    }

    public boolean deleteUserToDataBase(User user) {
        usersRepository.delete(user);
        return usersRepository.findById(user.getId()).isEmpty();
    }

}
