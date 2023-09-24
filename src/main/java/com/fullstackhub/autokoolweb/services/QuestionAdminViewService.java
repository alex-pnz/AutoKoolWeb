package com.fullstackhub.autokoolweb.services;

import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.repositories.AdminQuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionAdminViewService {

    private final AdminQuestionsRepository adminQuestionsRepository;

    public QuestionAdminViewService(AdminQuestionsRepository adminQuestionsRepository) {
        this.adminQuestionsRepository = adminQuestionsRepository;
    }

    public List<Question> getAll() {
        return adminQuestionsRepository.findAll();
    }

    public Question saveQuestionToDataBase(Question question) {
        return adminQuestionsRepository.save(question);
    }

    public boolean deleteQuestionFromDataBase(Question question) {
        adminQuestionsRepository.delete(question);
        return adminQuestionsRepository.findById(question.getIdquestions()).isEmpty();
    }

    public Question saveNewQuestionToDataBase(Question question) {
        return adminQuestionsRepository.save(question);
    }
}
