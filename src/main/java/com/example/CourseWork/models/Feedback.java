package com.example.CourseWork.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private LocalDate date;
    @Basic
    private String message;
    @ManyToOne
    private User author;

    public Feedback() {
        this.date = LocalDate.now();
    }

}
