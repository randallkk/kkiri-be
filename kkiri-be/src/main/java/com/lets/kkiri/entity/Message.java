package com.lets.kkiri.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @CreatedDate
    private LocalDateTime time;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Message(String message, LocalDateTime time, Member member) {
        this.message = message;
        this.time = time;
        this.member = member;
    }
}
