package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberExpenseRepository extends JpaRepository<MemberExpense, Long>, MemberExpenseRepositorySupport {
}
