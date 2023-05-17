package com.lets.kkiri.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class MoimExpense {
    @Id
    private Long id;
    private String place;
    @ColumnDefault("0")
    private Integer expense;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;
    private String receipt_url;

    @Builder
    public MoimExpense(String place, Integer expense, LocalDateTime time, String receipt_url) {
        this.place = place;
        this.expense = expense;
        this.time = time;
        this.receipt_url = receipt_url;
    }
}
