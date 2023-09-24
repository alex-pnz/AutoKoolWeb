package com.fullstackhub.autokoolweb.mappers;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.Result;
import com.fullstackhub.autokoolweb.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAdminViewMapper {

    public UserAdminViewIn toDto(User user) {
        UserAdminViewIn userAdminViewIn = new UserAdminViewIn();
        userAdminViewIn.setId(user.getId());
        userAdminViewIn.setUsername(user.getUsername());
        userAdminViewIn.setPassword(user.getPassword());
        userAdminViewIn.setRole(user.getRole());
        List<Result> results = user.getResults();
        if (results != null){
            userAdminViewIn.setPassed(results.stream().filter(r->r.getResult()==1).count());
            userAdminViewIn.setFailed(results.stream().filter(r->r.getResult()==0).count());
            userAdminViewIn.setIncomplete(results.stream().filter(r->r.getResult()==2).count());
            userAdminViewIn.setTotal((long) results.size());
        }
        return userAdminViewIn;
    }

}
