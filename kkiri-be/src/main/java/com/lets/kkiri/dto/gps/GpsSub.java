package com.lets.kkiri.dto.gps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpsSub {
    private Long moimId;
    private String kakaoId;
    private Double longitude;
    private Double latitude;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

    public GpsSub(Long moimId, String kakaoId, GpsPub gpsPub) {
        this.moimId = moimId;
        this.kakaoId = kakaoId;
        this.longitude = gpsPub.getLongitude();
        this.latitude = gpsPub.getLatitude();
        this.regDate = gpsPub.getRegDate();
    }
}