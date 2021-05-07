package com.example.CourseWork.services;

import com.example.CourseWork.exception.DuplicateProductInCartException;
import com.example.CourseWork.models.Cart;
import com.example.CourseWork.models.Product;
import com.example.CourseWork.models.User;
import com.example.CourseWork.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ProductService productService;

    @Override
    public void addToCart(Long productId, Long userId) {
        Cart cartEntity = findCartByUserId(userId);
        Product productEntity = productService.findProductById(productId);

        if (isPresentProductInCart(cartEntity, productEntity)) {
            throw new DuplicateProductInCartException();
        }

        cartEntity.addProduct(productEntity);
        cartRepository.save(cartEntity);
    }

    @Override
    public void deleteFromCart(Long productId, Long userId) {
        Cart cartEntity = findCartByUserId(userId);
        cartEntity.getProducts().removeIf(product -> product.getId().equals(productId));

        cartRepository.save(cartEntity);
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
        return findCartByUserId(userId).getProducts();
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.findById(id).ifPresent(cartRepository::delete);
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        User userEntity = userDetailsService.findUserById(userId);
        return cartRepository.findByUser(userEntity).orElseGet(() -> cartRepository.save(new Cart(userEntity)));
    }

    private boolean isPresentProductInCart(Cart cart, Product product) {
        return cart.getProducts().stream().anyMatch(prod -> prod.getId().equals(product.getId()));
    }
}
