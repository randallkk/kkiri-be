package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.common.util.S3Util;
import com.lets.kkiri.dto.ClovaOcrReq;
import com.lets.kkiri.dto.ReceiptOcrRes;
import com.lets.kkiri.dto.moim.MoimReceiptPostReq;
import com.lets.kkiri.repository.moim.MoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    @Value("${clova.ocr.secret}")
    private String secretKey;
    @Value("${clova.ocr.APIGW}")
    private String url;

    public ReceiptOcrRes readReceipt(MultipartFile file) throws IOException {
        try {
            String imageData = Base64.getEncoder().encodeToString(file.getBytes());

            // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); // 타임아웃 설정 5초
            factory.setReadTimeout(5000); // 타임아웃 설정 5초

            //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
            HttpClient httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(50)//최대 커넥션 수
                    .setMaxConnPerRoute(20).build();
            factory.setHttpClient(httpClient);

            // 2. RestTemplate 객체를 생성합니다.
            RestTemplate restTemplate = new RestTemplate(factory);

            // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
            HttpHeaders header = new HttpHeaders();
            header.add("X-OCR-SECRET", secretKey);
            header.add("Content-Type", "application/json");

            ClovaOcrReq clovaOcrReq = ClovaOcrReq.builder()
                    .requestId(UUID.randomUUID().toString().replaceAll("-", ""))
                    .timestamp(System.currentTimeMillis())
                    .format(file.getContentType().substring("image/".length()))
                    .data(imageData)
                    .name(file.getOriginalFilename().split("\\.")[0])
                    .build();
            HttpEntity<ClovaOcrReq> entity = new HttpEntity<>(clovaOcrReq, header);

            // 4. 요청 URL을 정의해줍니다.
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build(false);

            // 5. exchange() 메소드로 api를 호출합니다.
            ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, Map.class);

            // 6. 응답 코드를 확인합니다.
            if (response.getStatusCodeValue() == 200) {
                List<Map<String, Map<String, Map<String, Object>>>> images = (List) response.getBody().get("images");
                if ("ERROR".equals(images.get(0).get("inferResult"))) {
                    throw new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR, "영수증 사진이 아닙니다.");
                }
                Map<String, Map<String, Map<String, Object>>> result = (Map) images.get(0).get("receipt").get("result");
                Map<String, Map<String, Object>> storeInfo = result.get("storeInfo");
                Map<String, Map<String, Object>> paymentInfo = result.get("paymentInfo");
                Map<String, Map<String, Object>> totalPrice = result.get("totalPrice");

                String storeName = (String) storeInfo.get("name").get("text");
                LocalDateTime datetime = LocalDateTime.now();
                try {
                    datetime = LocalDateTime.parse( paymentInfo.get("date").get("text") + "T" + paymentInfo.get("time").get("text").toString().replace(" ", ""));
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                String price = (String) totalPrice.get("price").get("text");
                price = price.replaceAll("[^0-9]", "");
                return ReceiptOcrRes.builder()
                        .place(storeName)
                        .time(datetime)
                        .expense(Integer.parseInt(price))
                        .build();
            }
        } catch (NullPointerException e) {
            throw new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR, "영수증 인식에 실패했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw  new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR, "영수증 인식에 실패했습니다.");
    }

    public void addReceiptToMoim(String kakaoId, MoimReceiptPostReq moimReceiptPostReq) {

    }

}
