package com.lets.kkiri.dto.gps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpsSub {
    private Long moimId;
    private String memberKakaoId;
    private Double longitude;
    private Double latitude;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime regDate;
}