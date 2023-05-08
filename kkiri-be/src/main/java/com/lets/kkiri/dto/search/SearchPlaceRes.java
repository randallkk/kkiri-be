package com.lets.kkiri.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SearchPlaceRes {
    private KakaoSearchMetaDto meta;
    private List<KakaoPlaceDto> results;
}
