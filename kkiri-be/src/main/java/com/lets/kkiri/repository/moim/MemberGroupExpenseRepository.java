package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MemberGroupExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGroupExpenseRepository extends JpaRepository<MemberGroupExpense, Long> {
    @Query(nativeQuery = true, value = "select sum(me.expense)" +
            "from member_group_expense " +
            "join moim_expense me on member_group_expense.moim_expense_id = me.id " +
            "where me.moim_id = :moimId")
    Integer findMoimExpenseByMoimId(@Param("moimId") Long moimId);
}
