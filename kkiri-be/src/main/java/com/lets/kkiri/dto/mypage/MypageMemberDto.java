package com.lets.kkiri.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MypageMemberDto {
	private Integer rank;
	private String kakaoId;
	private String profileImage;
	private String nickname;
	private Long cnt; //만난횟수
}
