package com.lets.kkiri.controller;

import com.lets.kkiri.dto.search.SearchPlaceRes;
import com.lets.kkiri.service.KakaoSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final KakaoSearchService kakaoSearchService;

    @GetMapping()
    public SearchPlaceRes search(
            @RequestParam String query,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return kakaoSearchService.searchPlace(query, pageable);
    }
}
