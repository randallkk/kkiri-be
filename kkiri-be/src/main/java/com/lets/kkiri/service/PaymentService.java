package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.ClovaOcrReq;
import com.lets.kkiri.dto.ReceiptOcrRes;
import com.lets.kkiri.dto.member.MemberExpenditureDto;
import com.lets.kkiri.dto.moim.MoimReceiptPostReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberExpense;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.entity.MoimExpense;
import com.lets.kkiri.repository.member.MemberExpenseRepository;
import com.lets.kkiri.repository.member.MemberRepository;
import com.lets.kkiri.repository.moim.MoimExpenseRepository;
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
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    @Value("${clova.ocr.secret}")
    private String secretKey;
    @Value("${clova.ocr.APIGW}")
    private String url;

    private final MemberRepository memberRepository;
    private final MoimRepository moimRepository;
    private final MoimExpenseRepository moimExpenseRepository;
    private final MemberExpenseRepository memberExpenseRepository;

    @Transactional
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


    /**
     * 모임에 영수증 추가
     * 영수증을 추가하면 해당 멤버들의 정산 금액이 늘어나고, 나머지는 벌금에 추가된다.
     * @param moimId 모임 id
     * @param moimReceiptPostReq 영수증 정보
     *                           - memberKakaoIds : 영수증에 참여한 멤버들의 카카오 id
     *                           - expense : 영수증 금액
     *                           - time : 결제 시간
     *                           - place : 영수증 장소
     *                           - receiptUrl : 영수증 사진 url
     */
    @Transactional
    public void addReceiptToMoim(Long moimId, MoimReceiptPostReq moimReceiptPostReq) {
        List<String> memberKakaoIds = moimReceiptPostReq.getMemberKakaoIds();
        int memberCnt = memberKakaoIds.size();
        int expense = moimReceiptPostReq.getExpense();
        MoimExpense moimExpense = moimExpenseRepository.save(MoimExpense.builder()
                .moim(moimRepository.findById(moimId).orElseThrow(() -> new KkiriException(ErrorCode.MOIM_NOT_FOUND)))
                .expense(expense)
                .time(moimReceiptPostReq.getTime())
                .place(moimReceiptPostReq.getPlace())
                .receiptUrl(moimReceiptPostReq.getReceiptUrl())
                .build());
        for (String kakaoId : memberKakaoIds) {
            Member member = memberRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND));
            memberExpenseRepository.save(MemberExpense.builder()
                    .member(member)
                    .moimExpense(moimExpense)
                    .expenditure(expense / memberCnt)
                    .build());
        }
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new KkiriException(ErrorCode.MOIM_NOT_FOUND));
        moim.addLateFee(expense % memberCnt);
    }

    /**
     * 모임 구성원들의 정산 금액 목록 조회
     * @param moimId 모임 id
     * @return Map<String, Integer> : 카카오 id, 정산 금액
     */
    public Map<String, Integer> getEachExpenditureForMoim(Long moimId) {
        return memberExpenseRepository.findEachExpenditureForMoim(moimId).stream()
                .collect(Collectors.toMap(MemberExpenditureDto::getKakaoId, MemberExpenditureDto::getExpenditure));
    }

//    @Transactional
//    public void deleteReceiptFromMoim(Long moimId, Long moimExpenseId) {
//        MoimExpense moimExpense = moimExpenseRepository.findById(moimExpenseId)
//                .orElseThrow(() -> new KkiriException(ErrorCode.NOT_FOUND, "해당 영수증을 찾을 수 없습니다."));
//        List<MemberGroupExpense> memberGroupExpenses = memberGroupExpenseRepository.findAllByMoimExpense(moimExpense);
//        for (MemberGroupExpense memberGroupExpense : memberGroupExpenses) {
//            memberGroupExpenseRepository.delete(memberGroupExpense);
//        }
//        moimExpenseRepository.delete(moimExpense);
//    }


    /**
     * 모임의 총 정산 금액 조회
     * @param moimId 모임 id
     * @return 총 정산 금액
     */
    public int getMoimExpense(Long moimId) {
        try {
            return moimExpenseRepository.findMoimExpenseByMoimId(moimId);
        } catch (NullPointerException e) {
            return 0;
        }
    }


//    public Page<MoimExpenseDto> getMoimExpenseList(Long moimId, Pageable pageable) {
//        return moimExpenseRepository.findAllByMoimId(moimId, pageable).stream().map();
//    }
}
