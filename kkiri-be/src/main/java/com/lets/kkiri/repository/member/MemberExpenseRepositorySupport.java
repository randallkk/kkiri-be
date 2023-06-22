package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.member.MemberExpenditureDto;

import java.util.List;
import java.util.Map;

public interface MemberExpenseRepositorySupport {
    List<MemberExpenditureDto> findEachExpenditureForMoim(Long moimId);
    Map<Long, List<String>> findAllKakaoIdByMoimId(Long moimId);
}
