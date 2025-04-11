package com.lets.kkiri.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lets.kkiri.dto.gps.GpsDto;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // 타입 정보를 이름으로 구분
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // 이미 존재하는 필드 사용
        property = "type", // 실제 타입 구분에 사용할 필드 이름
        visible = true // 자식 클래스에서도 이 값이 보이게 함
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GpsDto.class, name = "GPS"),
        // 예: @JsonSubTypes.Type(value = MessageDto.class, name = "MESSAGE"),
})
public class BaseMessageDto {
    private String type;
    private Long moimId;
    private String kakaoId;
}
