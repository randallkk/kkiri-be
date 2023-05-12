package com.lets.kkiri.repository;

import com.lets.kkiri.dto.moim.MoimCardDto;
import com.lets.kkiri.entity.QMemberGroup;
import com.lets.kkiri.entity.QMoim;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MoimRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    QMoim qMoim = QMoim.moim;
    QMemberGroup qMemberGroup = QMemberGroup.memberGroup;

    public List<MoimCardDto> findMoimsByMemberIdAndDate(Long memberId, String date) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qMemberGroup.member.id.eq(memberId));
        builder.and(qMemberGroup.moim.id.eq(qMoim.id));

        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime parsingDate = LocalDateTime.parse(date, formatter);

            builder.and(qMoim.meetingAt.dayOfMonth().eq(parsingDate.getDayOfMonth()));
        }
        return jpaQueryFactory.select(
                        Projections.constructor(
                                MoimCardDto.class,
                                qMoim.id,
                                qMoim.name,
                                qMoim.placeName,
                                qMoim.meetingAt,
                                qMoim.lateFee
                        )
                ).from(qMoim, qMemberGroup)
                .where(builder)
                .fetch();
    }
}
