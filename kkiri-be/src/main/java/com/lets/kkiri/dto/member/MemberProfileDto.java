package com.lets.kkiri.dto.member;

import com.lets.kkiri.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberProfileDto {
    private Long id;
    private String kakaoId;
    private String email;
    private String nickname;
    private String profileImg;
    private String address;
    private String accountUrl;

    public MemberProfileDto(Member member) {
        this.id = member.getId();
        this.kakaoId = member.getKakaoId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profileImg = member.getProfileImg();
        this.address = member.getAddress();
        this.accountUrl = member.getAccountUrl();
    }
}
