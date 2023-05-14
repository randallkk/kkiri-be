package com.lets.kkiri.dto.gps;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ArriveRes {
    private Long moimId;
    private String kakaoId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime destinationTime;
    private Map<String, Long> ranking = new HashMap<>();

    public ArriveRes(Long moimId, String kakaoId, LocalDateTime destinationTime, Long rank, Long overall) {
        this.moimId = moimId;
        this.kakaoId = kakaoId;
        this.destinationTime = destinationTime;
        this.ranking.put("rank", rank);
        this.ranking.put("overall", overall);
    }

    public ArriveRes(Long moimId, String kakaoId, LocalDateTime destinationTime, Map<String, Long> ranking) {
        this.moimId = moimId;
        this.kakaoId = kakaoId;
        this.destinationTime = destinationTime;
        this.ranking = ranking;
    }
}
