package com.example.CourseWork.controller;

import com.example.CourseWork.models.User;
import com.example.CourseWork.services.CartService;
import com.example.CourseWork.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private CartService cartService;

    @GetMapping
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userDetailsService.findUserById(user.getId()));
        model.addAttribute("cart_size", cartService.getProductsByUserId(user.getId()).size());
        return "profile/profile";
    }

}
