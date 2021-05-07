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
    public void addToCart(Long productId, Long userId) { // Добавляем товар в корзину
        Cart cartEntity = findCartByUserId(userId); // Ищем корзину по ID пользователя
        Product productEntity = productService.findProductById(productId); // Ищем товар по ИД

        if (isPresentProductInCart(cartEntity, productEntity)) { // Если этот товар уже есть в корзине то выкидываем ошибку
            throw new DuplicateProductInCartException();
        }

        cartEntity.addProduct(productEntity); // Добавляем товар в коризну
        cartRepository.save(cartEntity); // Сохраняем корзину
    }

    @Override
    public void deleteFromCart(Long productId, Long userId) { // Удаляем товар из корзины
        Cart cartEntity = findCartByUserId(userId); // Ищем корзину по ID
        cartEntity.getProducts().removeIf(product -> product.getId().equals(productId)); // Вытаскиваем продукты из корзины, если в корзине есть ID, который пришлё в метод, то удаляем товар из корзины

        cartRepository.save(cartEntity); // Сохраняем корзину
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) { // Получаем все товары из корзины конкретного пользователя
        return findCartByUserId(userId).getProducts();
    }

    @Override
    public void deleteCart(Long id) { // Удаляем корзину из бд, если присутствует
        cartRepository.findById(id).ifPresent(cartRepository::delete);
    }

    @Override
    public Cart findCartByUserId(Long userId) { // Ищем корзину по ID пользователя
        User userEntity = userDetailsService.findUserById(userId);
        return cartRepository.findByUser(userEntity).orElseGet(() -> cartRepository.save(new Cart(userEntity))); // Если в бд есть корзина, возвращаем её, иначе создаём новую, сохраняем, и возвращаем её
    }

    private boolean isPresentProductInCart(Cart cart, Product product) { // Смотрим, есть ли товар в корзине
        return cart.getProducts().stream().anyMatch(prod -> prod.getId().equals(product.getId())); // Если ID товара в корзине равен ID товара, передоваемого в параметры то возвращаем true, иначе false
    }
}
