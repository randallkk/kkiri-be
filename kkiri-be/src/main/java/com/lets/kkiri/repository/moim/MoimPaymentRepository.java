package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimPaymentRepository extends JpaRepository<MoimPayment, Long>, MoimPaymentRepositorySupport {

}
