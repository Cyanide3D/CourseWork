package com.example.CourseWork.repository;

import com.example.CourseWork.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findOneById(Long id);
    List<Product> findAllByHoldAndSold(boolean hold, boolean sold);
    List<Product> findAllByExpireDateBeforeAndHold(LocalDate date, boolean hold);
}
