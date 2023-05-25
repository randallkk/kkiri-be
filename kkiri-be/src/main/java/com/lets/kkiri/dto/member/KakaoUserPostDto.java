package com.lets.kkiri.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lets.kkiri.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserPostDto {

    @JsonProperty("id")
    String kakaoId;
    String email;
    @JsonProperty("profileImageUrl")
    String profileImg;
    String nickname;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .profileImg(profileImg)
                .kakaoId(kakaoId)
                .build();
    }
}
