package com.example.CourseWork.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "borrowers")
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passport_id")
    private String passportId;
    @Basic
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrower")
    private List<Product> pledges;

    public Borrower() {
        pledges = new ArrayList<>();
    }

    public void addPledge(Product product) {
        pledges.add(product);
    }
}
