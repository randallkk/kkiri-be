package com.lets.kkiri.dto.member;

import com.lets.kkiri.entity.MemberDevice;
import com.lets.kkiri.entity.MemberTopic;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberTopicDto {
    private Long moimId;
    private MemberDevice memberDevice;
}
