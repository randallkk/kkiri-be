package com.lets.kkiri.dto.member;

import com.lets.kkiri.entity.Member;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberKakaoIdNameImageDto {
    private String kakaoId;
    private String profileImage;
    private String nickname;

    public static MemberKakaoIdNameImageDto of(Member member){
        return MemberKakaoIdNameImageDto.builder()
                .kakaoId(member.getKakaoId())
                .profileImage(member.getProfileImg())
                .nickname(member.getNickname())
                .build();
    }
}
