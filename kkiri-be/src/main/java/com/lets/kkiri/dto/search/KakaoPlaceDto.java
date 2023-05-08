package com.lets.kkiri.dto.search;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class KakaoPlaceDto {
    @SerializedName("road_address_name")
    private String addressName;
    @SerializedName("place_name")
    private String placeName;
    @SerializedName("x")
    private Double longitude;
    @SerializedName("y")
    private Double latitude;
}
