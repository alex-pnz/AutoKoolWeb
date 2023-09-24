package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.mappers.UserAdminViewMapper;
import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.AdminUsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminViewService {

    private final UserAdminViewMapper userAdminViewMapper;
    private final AdminUsersRepository adminUsersRepository;

    public UserAdminViewService(UserAdminViewMapper userAdminViewMapper, AdminUsersRepository adminUsersRepository) {
        this.userAdminViewMapper = userAdminViewMapper;
        this.adminUsersRepository = adminUsersRepository;
    }

    public List<UserAdminViewIn> getAllDto() {
        return adminUsersRepository.findAll().stream().map(userAdminViewMapper::toDto).toList();
    }

    public User saveUserToDataBase(User user) {

        User tempUser = adminUsersRepository.findByUsername(user.getUsername());

        if(tempUser == null || tempUser.getId().equals(user.getId())){
            return adminUsersRepository.save(user);
        }

        return null;

    }

    public boolean deleteUserToDataBase(User user) {
        adminUsersRepository.delete(user);
        return adminUsersRepository.findById(user.getId()).isEmpty();
    }

}
