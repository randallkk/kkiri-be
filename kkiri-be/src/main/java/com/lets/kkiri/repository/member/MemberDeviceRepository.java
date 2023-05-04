package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDeviceRepository extends JpaRepository<MemberDevice, Long> {
    List<MemberDevice> findByMemberId(Long memberId);
}
