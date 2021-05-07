package com.example.CourseWork.services;

import com.example.CourseWork.exception.NotEnoughMoneyToBuyException;
import com.example.CourseWork.models.Cart;
import com.example.CourseWork.models.Order;
import com.example.CourseWork.models.Product;
import com.example.CourseWork.models.User;
import com.example.CourseWork.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public void checkout(Long userId) {
        Cart cartEntity = cartService.findCartByUserId(userId);
        User userEntity = userDetailsService.findUserById(userId);
        List<Product> products = cartEntity.getProducts();

        userEntity.setMoney(getUserMoneyAfterOrder(userEntity, products));

        orderRepository.save(new Order(userEntity, getProductsWithSoldState(products)));
        cartService.deleteCart(cartEntity.getId());
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        User userEntity = userDetailsService.findUserById(userId);
        return orderRepository.findAllByUser(userEntity);
    }

    private int getUserMoneyAfterOrder(User user, List<Product> products) {
        int summaryProductPrice = getProductSumPrice(products);
        int userMoneyBeforeOrder = user.getMoney();

        if (userMoneyBeforeOrder < summaryProductPrice) {
            throw new NotEnoughMoneyToBuyException(); //TODO exception
        }

        return userMoneyBeforeOrder - summaryProductPrice;
    }

    private int getProductSumPrice(List<Product> products) {
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    private List<Product> getProductsWithSoldState(List<Product> products) {
        for (Product product : products) {
            product.setSold(true);
        }
        return new ArrayList<>(products);
    }
}
