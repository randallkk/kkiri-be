package com.lets.kkiri.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Document("message")
public class Message {
    @Id
    private String id;
    private String message;
    @CreatedDate
    private LocalDateTime time;
    private Long memberId;
    private Long moimId;

    @Builder
    public Message(String message, LocalDateTime time, Long memberId, Long moimId) {
        this.message = message;
        this.time = time;
        this.memberId = memberId;
        this.moimId = moimId;
    }
}
