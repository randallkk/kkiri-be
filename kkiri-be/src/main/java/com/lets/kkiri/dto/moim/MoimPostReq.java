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
    // 집합 장소
    private String placeName;
    // 위도
    private String latitude;
    // 경도
    private String longitude;
    private String date;
    private String time;
    @ColumnDefault("0")
    private Integer lateFee;

    public Moim toEntity(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
        return Moim.builder()
                .name(name)
                .placeName(placeName)
                .lat(latitude)
                .lng(longitude)
                .meetingAt(
                        LocalDateTime.parse(date + time, formatter)
                )
                .lateFee(lateFee)
                .build();
    }
}
