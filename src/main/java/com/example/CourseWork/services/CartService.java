package com.example.CourseWork.services;

import com.example.CourseWork.models.Cart;
import com.example.CourseWork.models.Product;

import java.util.List;

public interface CartService {

    void addToCart(Long productId, Long userId);
    void deleteFromCart(Long productId, Long userId);
    List<Product> getProductsByUserId(Long userId);
    void deleteCart(Long id);
    Cart findCartByUserId(Long userId);

}
