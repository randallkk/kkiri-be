package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
}
