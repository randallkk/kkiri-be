package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimExpense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MoimExpenseRepository extends JpaRepository<MoimExpense, Long>, MoimExpenseRepositorySupport {
    Page<MoimExpense> findAllByMoimId(Long moimId, Pageable pageable);
}