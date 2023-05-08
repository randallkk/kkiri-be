package com.lets.kkiri.dto.search;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class KakaoSearchMetaDto {
    @SerializedName("is_end")
    private Boolean isEnd;
    @SerializedName("pageable_count")
    private Integer pageableCount;
    @SerializedName("total_count")
    private Integer totalCount;
}
