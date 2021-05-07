package com.example.CourseWork.controller;

import com.example.CourseWork.models.User;
import com.example.CourseWork.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public String order(@AuthenticationPrincipal User user) {
        try {
            orderService.checkout(user.getId());
        } catch (Exception e) {
            return "redirect:/profile?error";
        }
        return "success";
    }
}