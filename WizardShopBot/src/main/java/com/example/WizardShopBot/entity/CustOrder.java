package com.example.WizardShopBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String customerOrder;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    private boolean isDone;

    public CustOrder(String customerOrder, Customer customer, boolean isDone) {
        this.customerOrder = customerOrder;
        this.customer = customer;
        this.isDone = isDone;
    }

    public CustOrder() {
    }
}
