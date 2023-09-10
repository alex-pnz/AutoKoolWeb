package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultsRepository extends JpaRepository<Result, Integer> {
}
