package com.example.sampleSQLByTime.Repository;


import com.example.sampleSQLByTime.Entity.MinuteProductionOfSecondValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinuteProductionOfSecondValueRepository extends JpaRepository<MinuteProductionOfSecondValue, Long> {
    MinuteProductionOfSecondValue findTopByOrderByTimestampDesc();
    List<MinuteProductionOfSecondValue> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}