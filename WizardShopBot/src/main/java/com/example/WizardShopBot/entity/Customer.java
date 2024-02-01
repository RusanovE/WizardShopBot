package com.example.WizardShopBot.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tg_nick;

    private String username;

    private long chatId;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustOrder> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    public Customer(String tg_nick, String username, long chatId) {
        this.tg_nick = tg_nick;
        this.username = username;
        this.chatId = chatId;
    }

    public Customer() {
    }



}
