package com.lets.kkiri.dto.moim;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MoimCardListGetRes {
    List<MoimCardDto> moimCardList;
}
