package com.lets.kkiri.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class ClovaOcrReq {
    private String version;
    private String requestId;
    private Long timestamp;
    private List<Map<String, String>> images = new ArrayList<>();

    @Builder
    public ClovaOcrReq(String requestId, Long timestamp, String format, String data, String name) {
        this.version = "V2";
        this.requestId = requestId;
        this.timestamp = timestamp;
        Map<String, String> image = new HashMap<>();
        image.put("format", format);
        image.put("data", data);
        image.put("name", name);
        this.images.add(image);
    }
}
