package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoimRepository extends JpaRepository<Moim, Long> {
    Optional<Moim> findById(Long id);
}
