package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
    Optional<Member> findByKakaoId(String kakaoId);
    Optional<Member> findById(Long id);
}
