package com.lets.kkiri.dto.gps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PointDto {
    private Double longitude;
    private Double latitude;
}
