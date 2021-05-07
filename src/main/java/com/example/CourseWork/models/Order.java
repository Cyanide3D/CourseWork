package com.example.CourseWork.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private boolean issued;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> product;

    public Order(User user, List<Product> product) {
        this.orderDate = LocalDate.now();
        this.user = user;
        this.product = product;
    }

}
