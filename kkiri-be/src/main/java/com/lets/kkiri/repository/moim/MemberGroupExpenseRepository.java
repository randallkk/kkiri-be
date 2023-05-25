package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MemberGroupExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupExpenseRepository extends JpaRepository<MemberGroupExpense, Long> , MemberGroupExpenseRepositorySupport {
}
