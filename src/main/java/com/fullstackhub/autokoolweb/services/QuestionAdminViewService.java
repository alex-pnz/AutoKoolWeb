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

        Question tempQuestion = adminQuestionsRepository.findByQuestion(question.getQuestion());

        if(tempQuestion == null || tempQuestion.getIdquestions().equals(question.getIdquestions())){
            return adminQuestionsRepository.save(question);
        }

        return null;
    }

    public boolean deleteQuestionFromDataBase(Question question) {
        adminQuestionsRepository.delete(question);
        return adminQuestionsRepository.findById(question.getIdquestions()).isEmpty();
    }
}
