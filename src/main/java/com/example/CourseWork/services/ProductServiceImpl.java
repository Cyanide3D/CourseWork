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
    public Product saveProduct(Product product, Borrower borrower) {
        prepareProductImages(product);

        Product productEntity = productRepository.save(product);
        Borrower borrowerEntity = borrowerRepository.findByPassportId(borrower.getPassportId()).orElseGet(() -> borrowerRepository.save(borrower));

        borrowerEntity.addPledge(productEntity);
        productEntity.setBorrower(borrowerEntity);

        borrowerRepository.save(borrowerEntity);
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
    public void activateOutdatedProducts() {
        LocalDate date = LocalDate.now().plusDays(15);
        List<Product> products = productRepository.findAllByDateOfTakeAfterAndHold(date, true);

        log.info("Start product activation process...");
        for (Product product : products) {
            activate(product.getId());
        }
        log.info("Activation process has been done.");
    }

    @Override
    public void activate(Long productId) {
        Product entityProduct = findProductById(productId);
        entityProduct.setHold(false);
        productRepository.save(entityProduct);
        //TODO We notify the user that the product is up for sale (Phone number)
    }

    @Override
    public List<Product> findAllTradableProducts() {
        return productRepository.findAllByHoldAndSold(false, false);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findOneById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional.ofNullable(findProductById(id)).ifPresent(productRepository::delete);
    }
}
