package com.lets.kkiri.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberDevicePostReq {
    @JsonProperty("deviceToken")
    String deviceToken;
}
