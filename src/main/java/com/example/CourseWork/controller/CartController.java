package com.example.CourseWork.controller;

import com.example.CourseWork.exception.DuplicateProductInCartException;
import com.example.CourseWork.models.User;
import com.example.CourseWork.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String cart(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("products", cartService.getProductsByUserId(user.getId()));
        model.addAttribute("user", user);
        return "cart/cart";
    }

    @PostMapping("/delete/{id}")
    public String deleteFromCart(@PathVariable("id") Long productId, @AuthenticationPrincipal User user) {
        cartService.deleteFromCart(productId, user.getId());
        return "redirect:/";
    }

    @PostMapping("/{id}")
    public String addToCart(@PathVariable("id") Long productId, @AuthenticationPrincipal User user) {
        try {
            cartService.addToCart(productId, user.getId());
        } catch (DuplicateProductInCartException e) {
            return "redirect:/cart?error";
        }
        return "redirect:/cart";
    }

}
