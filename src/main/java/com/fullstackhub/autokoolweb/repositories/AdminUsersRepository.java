package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminUsersRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users WHERE username =:name1", nativeQuery = true)
    User findByUsername(String name1);
}
