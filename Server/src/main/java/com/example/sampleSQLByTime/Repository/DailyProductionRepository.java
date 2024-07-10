package com.example.sampleSQLByTime.Repository;

import com.example.sampleSQLByTime.Entity.DailyProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyProductionRepository extends JpaRepository<DailyProduction, Long> {
    Optional<DailyProduction> findByDate(LocalDate date);
    List<DailyProduction> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
