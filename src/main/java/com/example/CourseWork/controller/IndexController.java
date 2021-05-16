package com.example.CourseWork.controller;

import com.example.CourseWork.models.Product;
import com.example.CourseWork.models.User;
import com.example.CourseWork.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        List<Product> prod = user != null
                ? user.getRole().equalsIgnoreCase("ADMIN") ? productService.findAllProducts() : productService.findAllTradableProducts()
                : productService.findAllTradableProducts();
        model.addAttribute("products", prod);
        return "index";
    }

}
