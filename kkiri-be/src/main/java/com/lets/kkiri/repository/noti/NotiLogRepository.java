package com.lets.kkiri.repository.noti;

import com.lets.kkiri.entity.NotiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiLogRepository extends JpaRepository<NotiLog, Long> {
    NotiLog save(NotiLog notiLog);
}
