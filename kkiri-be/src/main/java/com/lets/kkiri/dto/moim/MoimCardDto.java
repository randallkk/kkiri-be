package com.lets.kkiri.dto.moim;

import com.lets.kkiri.dto.member.MemberKakaoIdNameImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoimCardDto {
    private Long moimId;
    private String name;
    private String placeName;
    private String date;
    private String time;
    private Integer lateFee;
    private List<MemberKakaoIdNameImageDto> members;

    public MoimCardDto (Long moimId, String name, String placeName, LocalDateTime date, Integer lateFee) {
        this.moimId = moimId;
        this.name = name;
        this.placeName = placeName;
        this.date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
        this.time = DateTimeFormatter.ofPattern("HH:mm").format(date);
        this.lateFee = lateFee;
    }
}
