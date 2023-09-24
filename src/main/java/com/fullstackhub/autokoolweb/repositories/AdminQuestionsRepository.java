package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminQuestionsRepository extends JpaRepository<Question, Integer> {
}
