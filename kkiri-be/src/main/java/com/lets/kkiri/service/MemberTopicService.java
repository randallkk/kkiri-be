package com.lets.kkiri.service;

import com.lets.kkiri.dto.member.MemberTopicDto;
import com.lets.kkiri.entity.MemberDevice;
import com.lets.kkiri.entity.MemberTopic;
import com.lets.kkiri.repository.member.MemberTopicRepository;
import com.lets.kkiri.repository.member.MemberDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberTopicService {
    private final MemberTopicRepository memberTopicRepository;
    private final MemberDeviceRepository memberDeviceRepository;

    public void addMemberToMoim(Long memberId, Long moimId) {
        List<MemberDevice> tokens = memberDeviceRepository.findByMemberId(memberId);
        tokens.forEach((token) -> {
            memberTopicRepository.save(MemberTopic.of(
                    MemberTopicDto.builder()
                            .moimId(moimId)
                            .memberDevice(token)
                            .build()));
        });
    }
}
