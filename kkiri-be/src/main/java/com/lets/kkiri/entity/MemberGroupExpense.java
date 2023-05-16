package com.lets.kkiri.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MemberGroupExpense {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="moim_id"),
            @JoinColumn(name="member_id")
    })
    private MemberGroup memberGroup;
    @ManyToOne
    private MoimExpense moimExpense;
}
