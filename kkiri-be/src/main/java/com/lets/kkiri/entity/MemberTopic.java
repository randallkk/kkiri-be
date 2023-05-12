package com.lets.kkiri.entity;

import com.lets.kkiri.dto.member.MemberTopicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberDevice memberDevice;
    private String name;

    public static MemberTopic of(MemberTopicDto memberTopicDto) {
        return MemberTopic.builder()
                .memberDevice(memberTopicDto.getMemberDevice())
                .name(memberTopicDto.getMoimId().toString())
                .build();
    }
}
