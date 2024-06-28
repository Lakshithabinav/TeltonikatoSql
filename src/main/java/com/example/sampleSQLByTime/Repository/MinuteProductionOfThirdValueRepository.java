package com.example.sampleSQLByTime.Repository;


import com.example.sampleSQLByTime.Entity.MinuteProductionOfThirdValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinuteProductionOfThirdValueRepository extends JpaRepository<MinuteProductionOfThirdValue, Long> {
    List<MinuteProductionOfThirdValue> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

