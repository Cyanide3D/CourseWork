package com.example.CourseWork.services;

import com.example.CourseWork.models.Borrower;
import com.example.CourseWork.models.Product;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product, Borrower borrower);
    List<Product> findAllProducts();
    void activateOutdatedProducts();
    void activate(Long id);
    List<Product> findAllTradableProducts();
    Product findProductById(Long id);
    void deleteProduct(Long id);

}
