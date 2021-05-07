package com.example.CourseWork.controller;

import com.example.CourseWork.models.Payment;
import com.example.CourseWork.models.User;
import com.example.CourseWork.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping
    public String paymentPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("payment", new Payment());
        return "payment/payment";
    }

    @PostMapping
    public String pay(@ModelAttribute("payment") Payment payment, @AuthenticationPrincipal User user) {
        userDetailsService.increaseMoney(payment, user.getId());
        return "redirect:/profile?success";
    }

}
