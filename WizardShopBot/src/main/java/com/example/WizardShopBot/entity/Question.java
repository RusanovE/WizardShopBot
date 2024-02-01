package com.example.WizardShopBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    private boolean isAnswered;

    public Question(String question, Customer customer, boolean isAnswered) {
        this.question = question;
        this.customer = customer;
        this.isAnswered = isAnswered;
    }

    public Question() {
    }
}
