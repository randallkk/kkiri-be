package com.lets.kkiri.dto.moim;

import com.lets.kkiri.dto.member.MemberKakaoIdNameImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class MoimInfoGetRes {
    private Long moimId;
    private String name;
    private String placeName;
    private String latitude;
    private String longitude;
    private String date;
    private String time;
    private Integer lateFee;
    private List<MemberKakaoIdNameImageDto> members;
}
