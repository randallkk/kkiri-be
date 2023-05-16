package com.lets.kkiri.dto.mypage;

import java.time.LocalDateTime;
import java.util.List;

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
	private LocalDateTime startDay;
	private LocalDateTime endDay;
	private Long meetCnt;
	private String mostMem;
	private String mostLoc;
	private String mostTime;
	private List<MypageMemberDto> memList;
}
