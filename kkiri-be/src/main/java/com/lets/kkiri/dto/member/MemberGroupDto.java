package com.lets.kkiri.dto.member;

import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberGroup;
import com.lets.kkiri.entity.Moim;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberGroupDto {
    private Member member;
    private Moim moim;

    public static MemberGroup toEntity(Member member, Moim moim){
        return MemberGroup.builder()
                .member(member)
                .moim(moim)
                .build();
    }
}
