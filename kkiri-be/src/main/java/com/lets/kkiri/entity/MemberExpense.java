package com.lets.kkiri.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MemberExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private MoimExpense moimExpense;
    private Integer expenditure;
    private Boolean isPaid;

    @Builder
    public MemberExpense(Member member, MoimExpense moimExpense, int expenditure) {
        this.member = member;
        this.moimExpense = moimExpense;
        this.expenditure = expenditure;
        this.isPaid = false;
    }
}
