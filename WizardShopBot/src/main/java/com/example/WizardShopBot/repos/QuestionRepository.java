package com.example.WizardShopBot.repos;

import com.example.WizardShopBot.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> { }