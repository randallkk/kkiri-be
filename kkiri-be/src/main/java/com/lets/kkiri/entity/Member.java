package com.lets.kkiri.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String nickname;
    private String profileImg;
    private String address;
    private String accountUrl;
    private String kakaoId;
    @ColumnDefault("false")
    private Boolean isWithdrawal;
    @ColumnDefault("true")
    private Boolean moimAlarm;
    @ColumnDefault("true")
    private Boolean chatAlarm;
    @ColumnDefault("true")
    private Boolean meetAlarm;

    @Builder
    public Member(String email, String nickname, String profileImg, String address, String accountUrl, String kakaoId, Boolean isWithdrawal, Boolean moimAlarm, Boolean chatAlarm, Boolean meetAlarm) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.address = address;
        this.accountUrl = accountUrl;
        this.kakaoId = kakaoId;
        this.isWithdrawal = isWithdrawal;
        this.moimAlarm = moimAlarm;
        this.chatAlarm = chatAlarm;
        this.meetAlarm = meetAlarm;
    }
}
