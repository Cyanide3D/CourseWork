package com.example.CourseWork.services;

import com.example.CourseWork.models.Order;

import java.util.List;

public interface OrderService {

    void checkout(Long userId);
    List<Order> getOrdersByUserId(Long userId);

}
