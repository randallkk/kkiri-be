package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberTopicRepository extends JpaRepository<MemberTopic, Long> {
    MemberTopic save(MemberTopic memberTopic);
}
