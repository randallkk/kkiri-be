package com.lets.kkiri.dto.gps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GpsDto {
    private Long id;
    private Long roomId;
    private Long memberId;
    private Double longitude;
    private Double latitude;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime regDate;
}
