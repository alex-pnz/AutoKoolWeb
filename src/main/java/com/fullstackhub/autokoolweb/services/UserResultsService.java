package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.models.Result;
import com.fullstackhub.autokoolweb.repositories.UserResultsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserResultsService {
    private final UserResultsRepository userResultsRepository;

    public UserResultsService(UserResultsRepository userResultsRepository) {
        this.userResultsRepository = userResultsRepository;
    }

    public Result saveResultToDB(Result result) {
        return userResultsRepository.save(result);
    }
}
