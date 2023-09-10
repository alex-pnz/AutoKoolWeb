package com.fullstackhub.autokoolweb.repositories;

import com.fullstackhub.autokoolweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUsersRepository extends JpaRepository<User, Integer> {
}
