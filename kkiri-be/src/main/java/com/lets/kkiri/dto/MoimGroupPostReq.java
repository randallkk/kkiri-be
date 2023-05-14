package com.lets.kkiri.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MoimGroupPostReq {
    @JsonProperty("moimId")
    Long moimId;
}
