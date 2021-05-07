package com.example.CourseWork.repository;

import com.example.CourseWork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findOneById(Long id);
}
