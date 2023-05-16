package com.lets.kkiri.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.lets.kkiri.dto.mypage.MypageMemberDto;
import com.lets.kkiri.repository.member.MemberRepository;
import com.querydsl.core.Tuple;

import org.springframework.stereotype.Service;

import com.lets.kkiri.dto.mypage.MyPageResDto;
import com.lets.kkiri.dto.mypage.MypageDetailDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.repository.MemberGroupRepositorySupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

	private final MemberService memberService;
	private final MemberGroupRepositorySupport memberGroupRepositorySupport;
	private final MemberRepository memberRepository;

	public MyPageResDto getMyPage(String kakaoId) {
		MyPageResDto res = new MyPageResDto();
		LocalDate now = LocalDate.now();
		log.debug("startDate: {}", now);
		Member member = memberService.getMemberByKakaoId(kakaoId);
		MypageDetailDto month = getMonth(member, now.minusMonths(1).atStartOfDay(), now.atTime(LocalTime.MAX));
		return res;
	}

	private MypageDetailDto getMonth(Member member, LocalDateTime start, LocalDateTime end) {
		MypageDetailDto res = new MypageDetailDto();
		LocalDate startDay = start.toLocalDate();
		LocalDate endDay = end.toLocalDate();
		log.debug("startDate: {}, endDate: {}", start, end);
		Long meetCnt = getMyMoimCount(member, start, end);
		log.debug("meetCnt: {}", meetCnt);
		List<MypageMemberDto> memList = getMemList(member, start, end);
		String mostMem = null;
		if(memList != null) {
			mostMem = memList.get(0).getNickname();
		}
		String mostLoc = getMostLoc(member, start, end);
		log.debug("mostLoc: {}", mostLoc);
		return res;
	}

	private Long getMyMoimCount(Member member, LocalDateTime start, LocalDateTime end) {
		return memberGroupRepositorySupport.getCountMoimByMemberIdAndDate(member.getId(), start, end);
	}

	private List<MypageMemberDto> getMemList(Member member, LocalDateTime start, LocalDateTime end) {
		List<MypageMemberDto> res = new ArrayList<>();
		List<Tuple> list = memberGroupRepositorySupport.getMemberListByMemberIdAndDate(
			member.getId(), start, end);
		int idx = 1;
		for (Tuple tuple : list) {
			Long memberId = tuple.get(0, Long.class);
			if(memberId == member.getId()) continue;
			Member m = memberRepository.findById(memberId).get();
			res.add(MypageMemberDto.builder()
					.rank(idx++)
					.kakaoId(m.getKakaoId())
					.profileImage(m.getProfileImg())
					.nickname(m.getNickname())
					.cnt(tuple.get(1, Long.class))
					.build());
		}
		// for (MypageMemberDto dto : res) {
		// 	log.debug("dto: {}", dto);
		// }
		return res;
	}

	private String getMostLoc(Member member, LocalDateTime start, LocalDateTime end) {
		return memberGroupRepositorySupport.getMostLocByMemberIdAndDate(member.getId(), start, end);
	}

}
