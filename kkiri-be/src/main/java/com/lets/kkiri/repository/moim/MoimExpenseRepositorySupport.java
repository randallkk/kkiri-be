package com.lets.kkiri.repository.moim;

import com.lets.kkiri.dto.moim.MoimExpenseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoimExpenseRepositorySupport {
    Integer findMoimExpenseByMoimId(Long moimId);
    Page<MoimExpenseDto> findAllByMoimId(Long moimId, Pageable pageable);
}
