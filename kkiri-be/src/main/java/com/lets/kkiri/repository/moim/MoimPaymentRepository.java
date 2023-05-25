package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MoimPaymentRepository extends JpaRepository<MoimPayment, Long> {
    @Query(nativeQuery = true, value = "select mp.* " +
            "from moim_payment as mp " +
            "left join member_group mg on mp.member_id = mg.member_id and mp.moim_id = mg.moim_id " +
            "left join member m on mg.member_id = m.id " +
            "where mp.moim_id = :moimId and m.kakao_id = :kakaoId")
    MoimPayment findByMoimIdAndKakaoId(@Param("moimId") Long moimId, @Param("kakaoId") String kakaoId);

    @Query(nativeQuery = true, value = "select mp.* " +
            "from moim_payment as mp " +
            "join member_group mg on mp.member_id = mg.member_id and mp.moim_id = mg.moim_id " +
            "where mp.moim_id = :moimId")
    List<MoimPayment> findAllByMoimId(@Param("moimId") Long moimId);
}
