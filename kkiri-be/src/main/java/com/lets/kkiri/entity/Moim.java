package com.lets.kkiri.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Moim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // 모임 링크
    private String link;
    // 집합 장소
    private String place;
    // 위도
    private String lat;
    // 경도
    private String lng;
    private LocalDateTime meetingAt;
    @ColumnDefault("0")
    private Integer lateFee;

    @Builder
    public Moim(String name, String link, String place, String lat, String lng, LocalDateTime meetingAt, Integer lateFee) {
        this.name = name;
        this.link = link;
        this.place = place;
        this.lat = lat;
        this.lng = lng;
        this.meetingAt = meetingAt;
        this.lateFee = lateFee;
    }
}
