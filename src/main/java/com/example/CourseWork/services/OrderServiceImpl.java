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
    public void checkout(Long userId) { // Совершаем покупку
        Cart cartEntity = cartService.findCartByUserId(userId); // Ищем корзину по ИД пользователя
        User userEntity = userDetailsService.findUserById(userId); // Ищем пользователя по ИД
        List<Product> products = cartEntity.getProducts();

        userEntity.setMoney(getUserMoneyAfterOrder(userEntity, products)); // Устанавлеваем пользователю деньги, оставшиеся после покупки

        orderRepository.save(new Order(userEntity, getProductsWithSoldState(products))); // Сохраняем сущность с покупкой
        cartService.deleteCart(cartEntity.getId()); // Удаляем корзину
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) { // Не используется, делалось для истории покупок, лень верстать. Возвращает все покупки пользователя
        User userEntity = userDetailsService.findUserById(userId);
        return orderRepository.findAllByUser(userEntity);
    }

    private int getUserMoneyAfterOrder(User user, List<Product> products) { // Выщитывает сумму денег пользователя после совершения покупки
        int summaryProductPrice = getProductSumPrice(products);
        int userMoneyBeforeOrder = user.getMoney();

        if (userMoneyBeforeOrder < summaryProductPrice) { // Если денег не достаточно то выкидываем ошибку
            throw new NotEnoughMoneyToBuyException(); //TODO exception
        }

        return userMoneyBeforeOrder - summaryProductPrice; // Возвращаем разницу между суммой денег пользователя и суммой товаров
    }

    private int getProductSumPrice(List<Product> products) { // Выщитываем суммарную стоимость всех товаров
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    private List<Product> getProductsWithSoldState(List<Product> products) { // Устанавлеваем купленным продуктам статус SOLD
        for (Product product : products) {
            product.setSold(true);
        }
        return new ArrayList<>(products);
    }
}
