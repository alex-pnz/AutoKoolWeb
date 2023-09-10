package com.fullstackhub.autokoolweb.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer result;

    public Result() {
    }

    public Result(Integer resultId, User user, Integer result) {
        this.resultId = resultId;
        this.user = user;
        this.result = result;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(resultId, result.resultId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId);
    }
}
