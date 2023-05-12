package com.lets.kkiri.dto.gps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GpsPub {
    private Double longitude;
    private Double latitude;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;
}