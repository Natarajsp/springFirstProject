package com.example.springFirstProject.repository;

import com.example.springFirstProject.Beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
