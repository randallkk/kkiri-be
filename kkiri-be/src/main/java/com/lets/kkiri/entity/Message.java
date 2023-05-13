package com.lets.kkiri.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.lets.kkiri.dto.moim.MoimSessionReq;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("message")
public class Message {
    @Id
    private String id;
    private String message;
    @CreatedDate
    private LocalDateTime time;
    private String kakaoId;
    private String nickname;
    private Long moimId;
    private MoimSessionReq.MoimSessionType messageType;

    @Builder
    public Message(String message, LocalDateTime time, String kakaoId, String nickname, Long moimId, MoimSessionReq.MoimSessionType messageType) {
        this.message = message;
        this.time = time;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.moimId = moimId;
        this.messageType = messageType;
    }
}
