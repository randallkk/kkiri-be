package com.lets.kkiri.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class MoimExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Moim moim;
    private String place;
    @ColumnDefault("0")
    private Integer expense;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;
    private String receiptUrl;

    @Builder
    public MoimExpense(Moim moim, String place, Integer expense, LocalDateTime time, String receiptUrl) {
        this.moim = moim;
        this.place = place;
        this.expense = expense;
        this.time = time;
        this.receiptUrl = receiptUrl;
    }
}
