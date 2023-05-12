package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.member.MemberKakaoIdNameImageDto;
import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.dto.moim.MoimInfoGetRes;
import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberTopic;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.MemberTopicRepositorySupport;
import com.lets.kkiri.repository.moim.MoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoimService {
    private final MemberService memberService;
    private final MoimRepository moimRepository;
    private final MemberTopicService memberTopicService;
    private final MemberTopicRepositorySupport memberTopicRepositorySupport;

    public Long addMoim(String kakaoId, MoimPostReq moimPostReq) {
        Moim moim = moimRepository.save(moimPostReq.toEntity());

        Long memberId = memberService.getMemberByKakaoId(kakaoId).getId();
        memberTopicService.addMemberToMoim(memberId, moim.getId());

        return moim.getId();
    }

    public MoimInfoGetRes findMoimById(Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new KkiriException(ErrorCode.MOIM_NOT_FOUND));
        String[] meetingAt = moim.getMeetingAt().toString().split("T");
        MoimInfoGetRes.MoimInfoGetResBuilder res = MoimInfoGetRes.builder()
                .moimId(moim.getId())
                .name(moim.getName())
                .date(meetingAt[0])
                .time(meetingAt[1])
                .placeName(moim.getPlaceName())
                .lateFee(moim.getLateFee())
                .latitude(moim.getLat())
                .longitude(moim.getLng());

        List<MemberKakaoIdNameImageDto> members = findMembersByMoimId(moimId).stream().collect(ArrayList::new, (list, memberProfileDto) -> {
            list.add(MemberKakaoIdNameImageDto.builder()
                    .kakaoId(memberProfileDto.getKakaoId())
                    .nickname(memberProfileDto.getNickname())
                    .profileImage(memberProfileDto.getProfileImg())
                    .build());
        }, ArrayList::addAll);

        res.members(members);

        return res.build();
    }

    public List<MemberProfileDto> findMembersByMoimId(Long moimId){
        return memberTopicRepositorySupport.findMembersByMoimId(moimId);
    }
}
