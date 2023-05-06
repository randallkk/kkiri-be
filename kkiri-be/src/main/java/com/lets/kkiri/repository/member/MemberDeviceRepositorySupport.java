package com.lets.kkiri.repository.member;

//import com.lets.kkiri.entity.QMemberDevice;
//import com.querydsl.jpa.impl.JPAQueryFactory;
import com.lets.kkiri.entity.QMemberDevice;
import com.lets.kkiri.entity.QMemberTopic;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberDeviceRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    QMemberDevice qMemberDevice = QMemberDevice.memberDevice;
    QMemberTopic qMemberTopic = QMemberTopic.memberTopic;

    public List<String> findTokenListByMemberId(Long memberId) {
        return jpaQueryFactory.select(qMemberDevice.token)
                .from(qMemberDevice)
                .where(qMemberDevice.member.id.eq(memberId))
                .fetch();
    }

    public List<String> findTokenListByMoimId(Long senderId, String moimId) {
        return jpaQueryFactory.select(qMemberDevice.token)
                .from(qMemberDevice, qMemberTopic)
                .where(
                        qMemberDevice.member.id.ne(senderId),
                        qMemberTopic.name.eq(moimId),
                        qMemberDevice.id.eq(qMemberTopic.memberDevice.id)
                )
                .fetch();
    }
}
