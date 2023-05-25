package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MemberGroupExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGroupExpenseRepository extends JpaRepository<MemberGroupExpense, Long> {
}
