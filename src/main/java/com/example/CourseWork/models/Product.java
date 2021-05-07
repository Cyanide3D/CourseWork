package com.example.CourseWork.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "date_of_take")
    private LocalDate dateOfTake;
    @Column(name = "expire_date")
    private LocalDate expireDate;
    @Basic
    private String description;
    @Transient
    private String rawImages;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;
    @Basic
    private boolean hold;
    @Basic
    private boolean sold;
    @Basic
    private Integer price;
    @Column(name = "address_pawnshop")
    private String addressPawnshop;
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    public Product() {
        this.dateOfTake = LocalDate.now();
        this.expireDate = LocalDate.now().plusDays(15);
        this.hold = true;
    }
}
