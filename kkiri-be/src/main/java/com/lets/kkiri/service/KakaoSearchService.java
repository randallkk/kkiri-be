//package com.lets.kkiri.service;
//
//import com.google.gson.*;
//import com.lets.kkiri.dto.search.KakaoPlaceDto;
//import com.lets.kkiri.dto.search.KakaoSearchMetaDto;
//import com.lets.kkiri.dto.search.SearchPlaceRes;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class KakaoSearchService {
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String KAKAO_KEY;
//    public SearchPlaceRes searchPlace(String query, Pageable pageable) {
//        SearchPlaceRes res = null;
//        String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json";
//        try {
//            query = URLEncoder.encode(query, "UTF-8");
//            String urlStr = apiUrl + "?query=" + query + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
//            URL url = new URL(urlStr);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_KEY);
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                StringBuilder response = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//                br.close();
//
//                // DTO에 매핑
//                Gson gson = new GsonBuilder().create();
//
//                // API 응답을 JSON으로 파싱
//                JsonParser parser = new JsonParser();
//                JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();
//
//                // meta 정보를 확인하고 필요한 필드를 가져옴
//                JsonObject meta = jsonObject.get("meta").getAsJsonObject();
//                KakaoSearchMetaDto metaRes =
//                        gson.fromJson(meta, KakaoSearchMetaDto.class);
//
//                // documents 정보를 가져옴
//                JsonArray documents = jsonObject.get("documents").getAsJsonArray();
//
//                List<KakaoPlaceDto> places = new ArrayList<>();
//
//                for (JsonElement jsonElement : documents) {
//                    KakaoPlaceDto place = gson.fromJson(jsonElement, KakaoPlaceDto.class);
//                    places.add(place);
//                }
//
//                res = SearchPlaceRes.builder()
//                        .meta(metaRes)
//                        .results(places)
//                        .build();
//            } else {
//                System.out.println("Error: " + responseCode);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return res;
//    }
//}
