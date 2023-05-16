package com.lets.kkiri.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResDto {
	private MypageDetailDto month;
	private MypageDetailDto trimestral;
	private MypageDetailDto semiannual;
	private MypageDetailDto annual;
}
