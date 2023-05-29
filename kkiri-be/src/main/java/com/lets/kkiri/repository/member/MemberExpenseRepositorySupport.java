package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.member.MemberExpenditureDto;

import java.util.List;

public interface MemberExpenseRepositorySupport {
    List<MemberExpenditureDto> findEachExpenditureForMoim(Long moimId);
}
