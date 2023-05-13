package com.lets.kkiri.entity;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.lets.kkiri.dto.moim.MoimSessionReq;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("message")
public class Message {
    @Transient
    public static final String SEQUENCE_NAME = "message_sequence";
    @Id
    private String id;
    private Long seq;
    private String message;
    @CreatedDate
    private LocalDateTime time;
    private String kakaoId;
    private String nickname;
    private Long moimId;
    private MoimSessionReq.MoimSessionType messageType;

    @Builder
    public Message(Long seq, String message, LocalDateTime time, String kakaoId, String nickname, Long moimId, MoimSessionReq.MoimSessionType messageType) {
        this.seq = seq;
        this.message = message;
        this.time = time;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.moimId = moimId;
        this.messageType = messageType;
    }
}
