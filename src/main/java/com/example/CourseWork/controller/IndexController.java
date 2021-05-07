package com.example.CourseWork.controller;

import com.example.CourseWork.models.User;
import com.example.CourseWork.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("products", productService.findAllTradableProducts());
        return "index";
    }

}
