package com.lets.kkiri.dto.moim;

import com.lets.kkiri.dto.BaseMessageDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoimSessionReq {
    public enum MoimSessionType {   // message, gps, emoji, 재촉 ...
        MESSAGE, GPS, EMOJI, URGENT, JOIN
    }
    private MoimSessionType type;
    private BaseMessageDto content;
}
