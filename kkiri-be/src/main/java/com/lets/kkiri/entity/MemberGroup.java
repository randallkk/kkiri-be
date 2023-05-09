package com.lets.kkiri.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@IdClass(MemberGroupId.class)
public class MemberGroup {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Member member;
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Moim moim;
    private Integer rank;
    private LocalDateTime destination_time;

}
