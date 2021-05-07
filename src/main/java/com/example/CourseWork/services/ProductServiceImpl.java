package com.example.CourseWork.services;

import com.example.CourseWork.models.Borrower;
import com.example.CourseWork.models.Product;
import com.example.CourseWork.repository.BorrowerRepository;
import com.example.CourseWork.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BorrowerRepository borrowerRepository;

    @Override
    public Product saveProduct(Product product, Borrower borrower) { // Сохраняем товар
        prepareProductImages(product); // Превращаем строку с картинками в удобный массив

        Product productEntity = productRepository.save(product); // Сохраняем товар
        Borrower borrowerEntity = borrowerRepository.findByPassportId(borrower.getPassportId()).orElseGet(() -> borrowerRepository.save(borrower)); // Сохраняем закладчика, или получаем имеющегося

        borrowerEntity.addPledge(productEntity); // Добавляем товар закладчику
        productEntity.setBorrower(borrowerEntity); // Устанавливаем товару закладчика

        borrowerRepository.save(borrowerEntity); // Сохраняем обе сущности
        return productRepository.save(productEntity);
    }

    private void prepareProductImages(Product product) {
        String[] split = product.getRawImages().split(",");
        product.setImages(new ArrayList<>(Arrays.asList(split)));
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void activateOutdatedProducts() { // Автоматичекое выставление товаров на продажу, метод будет выполняться раз в день
        List<Product> products = productRepository.findAllByExpireDateBeforeAndHold(LocalDate.now(), true);

        log.info("Start product activation process...");
        for (Product product : products) {
            activate(product.getId());
        }
        log.info("Activation process has been done.");
    }

    @Override
    public void activate(Long productId) { // Устанавливаем статус HOLD на false
        Product entityProduct = findProductById(productId);
        entityProduct.setHold(false);
        productRepository.save(entityProduct);
        //TODO We notify the user that the product is up for sale (Phone number)
    }

    @Override
    public List<Product> findAllTradableProducts() { // Получаем все товары, которые можно купить
        return productRepository.findAllByHoldAndSold(false, false);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findOneById(id);
    }

    @Override
    public void deleteProduct(Long id) { // Удаляем продукт по ИД, если таковой имеется
        Optional.ofNullable(findProductById(id)).ifPresent(productRepository::delete);
    }
}
