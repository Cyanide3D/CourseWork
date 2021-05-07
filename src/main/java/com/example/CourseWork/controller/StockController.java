package com.example.CourseWork.controller;

import com.example.CourseWork.models.Borrower;
import com.example.CourseWork.models.Product;
import com.example.CourseWork.models.User;
import com.example.CourseWork.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String stockForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("borrower", new Borrower());
        return "stock/form";
    }

    @PostMapping("/activate/{id}")
    public String activate(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        User usr = Optional.ofNullable(user).orElse(new User("USER"));
        if (usr.getRole().equalsIgnoreCase("admin")) {
            productService.activate(id);
        }

        return "redirect:/stock/" + id.intValue();
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User user) {
        Product product = productService.findProductById(id);
        User usr = Optional.ofNullable(user).orElse(new User("USER"));

        if (usr.getRole().equalsIgnoreCase("USER") && product.isHold()) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("product", product);

        return "stock/show";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute("product") Product product, @ModelAttribute("borrower") Borrower borrower) {
        Product entity = productService.saveProduct(product, borrower);
        return "redirect:/stock/" + entity.getId().intValue();
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

}
