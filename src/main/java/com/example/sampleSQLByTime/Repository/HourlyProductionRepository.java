package com.example.sampleSQLByTime.Repository;

import com.example.sampleSQLByTime.Entity.HourlyProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HourlyProductionRepository extends JpaRepository<HourlyProduction, Long> {
    List<HourlyProduction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<HourlyProduction> findByTimestamp(LocalDateTime timestamp);
}
