package com.example.CourseWork.repository;

import com.example.CourseWork.models.Order;
import com.example.CourseWork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

}
