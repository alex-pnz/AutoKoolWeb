package com.fullstackhub.autokoolweb.dtos;

import com.fullstackhub.autokoolweb.models.Result;
import com.fullstackhub.autokoolweb.models.User;

import java.util.List;

public class UserAdminViewIn {

    private Integer id;
    private String username;
    private String password;
    private User.Role role;
    private List<Result> results;
    private Long passed;
    private Long failed;
    private Long incomplete;
    private Long total;

    public UserAdminViewIn(Integer id, String username, String password, User.Role role, List<Result> results, Long passed, Long failed, Long incomplete, Long total) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.results = results;
        this.passed = passed;
        this.failed = failed;
        this.incomplete = incomplete;
        this.total = total;
    }

    public UserAdminViewIn() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Long getPassed() {
        return passed;
    }

    public void setPassed(Long passed) {
        this.passed = passed;
    }

    public Long getFailed() {
        return failed;
    }

    public void setFailed(Long failed) {
        this.failed = failed;
    }

    public Long getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(Long incomplete) {
        this.incomplete = incomplete;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
