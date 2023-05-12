package com.lets.kkiri.dto.moim;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoimLinkPostReq {
    private Long moimId;
    private String link;
}
