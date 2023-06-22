package com.lets.kkiri.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class MemberGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Moim moim;
    private Integer rank;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime destination_time;

    @Builder
    public MemberGroup (Member member, Moim moim) {
        this.member = member;
        this.moim = moim;
    }

    public void changeRank(Long rank) {
        this.rank = rank.intValue() + 1;
    }
}
