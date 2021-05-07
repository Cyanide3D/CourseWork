package com.example.CourseWork.repository;

import com.example.CourseWork.models.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    Optional<Borrower> findByPassportId(String passportId);

}
