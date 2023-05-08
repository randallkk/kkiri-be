package com.lets.kkiri.service;

import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.moim.MoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoimService {
    private final MoimRepository moimRepository;

    public void addMoim(MoimPostReq moimPostReq) {
        moimRepository.save(moimPostReq.toEntity());
    }
}
