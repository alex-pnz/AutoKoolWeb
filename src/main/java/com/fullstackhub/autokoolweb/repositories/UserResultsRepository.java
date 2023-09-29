package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResultsRepository extends JpaRepository<Result, Integer> {
}
