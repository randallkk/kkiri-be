package com.lets.kkiri.dto.moim;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.type.DateTime;
import com.lets.kkiri.entity.Moim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@Data
public class MoimPostReq {
    private String name;
    // 모임 링크
    private String link;
    // 집합 장소
    private String place;
    // 위도
    private String lat;
    // 경도
    private String lng;
    private String meetingAt;
    @ColumnDefault("0")
    private Integer lateFee;

    public Moim toEntity(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return Moim.builder()
                .name(name)
                .link(link)
                .place(place)
                .lat(lat)
                .lng(lng)
                .meetingAt(
                        LocalDateTime.parse(meetingAt, formatter)
                )
                .lateFee(lateFee)
                .build();
    }

    public static MoimPostReq of(Moim moim){
        return MoimPostReq.builder()
                .name(moim.getName())
                .link(moim.getLink())
                .place(moim.getPlace())
                .lat(moim.getLat())
                .lng(moim.getLng())
                .meetingAt(moim.getMeetingAt().toString())
                .lateFee(moim.getLateFee())
                .build();
    }
}
