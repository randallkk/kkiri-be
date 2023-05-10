package com.lets.kkiri.dto.moim;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoimSessionReq {
    public enum MoimSessionType {   // message, gps, emoji, 재촉 ...
        MESSAGE, GPS, EMOJI, URGENT, OPEN
    }
    private MoimSessionType type;
    private Object content;
}
