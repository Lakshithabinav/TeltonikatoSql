package com.example.sampleSQLByTime.Repository;


import com.example.sampleSQLByTime.Entity.MinuteProductionOfFirstValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinuteProductionOfFirstValueRepository extends JpaRepository<MinuteProductionOfFirstValue, Long> {
    List<MinuteProductionOfFirstValue> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
