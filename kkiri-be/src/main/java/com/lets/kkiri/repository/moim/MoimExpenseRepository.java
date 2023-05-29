package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimExpenseRepository extends JpaRepository<MoimExpense, Long>, MoimExpenseRepositorySupport {
}