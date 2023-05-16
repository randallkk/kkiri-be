package com.lets.kkiri.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MoimPayment {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="moim_id"),
            @JoinColumn(name="member_id")
    })
    private MemberGroup memberGroup;
    @ColumnDefault("0")
    private Integer expenditure;
    @ColumnDefault("false")
    private Boolean status;
}
