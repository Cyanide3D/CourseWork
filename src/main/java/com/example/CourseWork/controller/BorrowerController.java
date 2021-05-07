package com.example.CourseWork.controller;

import com.example.CourseWork.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/borrower")
public class BorrowerController {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("borrower", borrowerRepository.findById(id).get());
        return "borrower/borrower";
    }

}
