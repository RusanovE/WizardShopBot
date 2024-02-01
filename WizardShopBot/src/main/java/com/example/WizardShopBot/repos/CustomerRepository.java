package com.example.WizardShopBot.repos;

import com.example.WizardShopBot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository <Customer,Long> {
    Customer findByChatId(long chatId);
}
