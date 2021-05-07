package com.example.CourseWork.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public Cart(User user) {
        this.products = new ArrayList<>();
        this.user = user;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
