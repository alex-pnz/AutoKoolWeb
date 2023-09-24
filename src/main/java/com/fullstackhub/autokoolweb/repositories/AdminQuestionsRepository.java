package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminQuestionsRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM questions WHERE question =:question1", nativeQuery = true)
    Question findByQuestion(String question1);
}
