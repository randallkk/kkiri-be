package com.lets.kkiri.service;

import com.google.type.DateTime;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.MoimGroupPostReq;
import com.lets.kkiri.dto.member.MemberGroupDto;
import com.lets.kkiri.dto.member.MemberKakaoIdNameImageDto;
import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.dto.moim.MoimCardDto;
import com.lets.kkiri.dto.moim.MoimInfoGetRes;
import com.lets.kkiri.dto.moim.MoimLinkPostReq;
import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.MemberGroupRepositorySupport;
import com.lets.kkiri.repository.MoimRepositorySupport;
import com.lets.kkiri.repository.member.MemberGroupRepository;
import com.lets.kkiri.repository.moim.MoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoimService {
    private final MemberService memberService;
    private final MoimRepository moimRepository;
    private final MemberTopicService memberTopicService;
    private final MoimRepositorySupport moimRepositorySupport;
    private final MemberGroupRepository memberGroupRepository;
    private final MemberGroupRepositorySupport memberGroupRepositorySupport;

    public Long addMoim(String kakaoId, MoimPostReq moimPostReq) {
        Moim moim = moimRepository.save(moimPostReq.toEntity());

        Member member = memberService.getMemberByKakaoId(kakaoId);
        memberGroupRepository.save(MemberGroupDto.toEntity(member, moim));
        // 멤버의 디바이스 모임 구독
        memberTopicService.addMemberToMoim(member.getId(), moim.getId());

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
        return memberGroupRepositorySupport.findMembersByMoimId(moimId);
    }

    public void addLinkToMoim(MoimLinkPostReq moimPostReq) {
        Moim moim = moimRepository.findById(moimPostReq.getMoimId()).orElseThrow(() -> new KkiriException(ErrorCode.MOIM_NOT_FOUND));
        moim.addLinkToMoim(moimPostReq.getLink());

        try {
            moimRepository.save(moim);
        } catch (Exception e) {
            throw new KkiriException(ErrorCode.MOIM_CREATE_FAIL);
        }
    }

    public List<MoimCardDto> findMoimsByKakaoId(String kakaoId, LocalDate date) {
        Member member = memberService.getMemberByKakaoId(kakaoId);

        List<MoimCardDto> moimCards = moimRepositorySupport.findMoimsByMemberIdAndDate(member.getId(), date);

        moimCards.forEach((moinCard) -> {
            List<MemberKakaoIdNameImageDto> members = findMembersByMoimId(moinCard.getMoimId()).stream().collect(ArrayList::new, (list, memberProfileDto) -> {
                list.add(MemberKakaoIdNameImageDto.builder()
                        .kakaoId(memberProfileDto.getKakaoId())
                        .nickname(memberProfileDto.getNickname())
                        .profileImage(memberProfileDto.getProfileImg())
                        .build());
            }, ArrayList::addAll);
            moinCard.setMembers(members);
        });

        return moimCards;
    }

    public Long addMemberToMoim(String kakaoId, MoimGroupPostReq moimGroupPostReq) {
        Member member = memberService.getMemberByKakaoId(kakaoId);
        Moim moim = moimRepository.findById(moimGroupPostReq.getMoimId()).orElseThrow(() -> new KkiriException(ErrorCode.MOIM_NOT_FOUND));
        return memberGroupRepository.save(MemberGroupDto.toEntity(member, moim)).getMoim().getId();
    }
}
