package com.example.CourseWork.controller;

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
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsServiceImpl userService;

    @Autowired
    public AuthController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registrationForm(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String registration(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/login";
    }

}
