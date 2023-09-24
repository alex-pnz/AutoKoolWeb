package com.fullstackhub.autokoolweb.models;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idquestions;
    private String question;
    private String image;
    private String option1; //TODO make class Pair option-answer
    private String option2;
    private String option3;
    private Integer answer1;
    private Integer answer2;
    private Integer answer3;

    public Question(Integer idquestions, String question, String image, String option1, String option2, String option3, Integer answer1, Integer answer2, Integer answer3) {
        this.idquestions = idquestions;
        this.question = question;
        this.image = image;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    public Question() {
    }

    public Integer getIdquestions() {
        return idquestions;
    }

    public void setIdquestions(Integer idquestions) {
        this.idquestions = idquestions;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public Integer getAnswer1() {
        return answer1;
    }

    public void setAnswer1(Integer answer1) {
        this.answer1 = answer1;
    }

    public Integer getAnswer2() {
        return answer2;
    }

    public void setAnswer2(Integer answer2) {
        this.answer2 = answer2;
    }

    public Integer getAnswer3() {
        return answer3;
    }

    public void setAnswer3(Integer answer3) {
        this.answer3 = answer3;
    }
}
