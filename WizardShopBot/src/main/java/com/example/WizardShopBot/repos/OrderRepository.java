package com.example.WizardShopBot.repos;

import com.example.WizardShopBot.entity.CustOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <CustOrder,Long> {
}