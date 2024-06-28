package com.example.sampleSQLByTime.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.Entity.DailyProduction;
import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Repository.DailyProductionRepository;
import com.example.sampleSQLByTime.Repository.HourlyProductionRepository;

@Service
public class DataTotalService {
    @Autowired
    private HourlyProductionRepository hourlyProductionRepository;
    @Autowired
    private DailyProductionRepository dailyProductionRepository;

    // Method to fetch data for the current hour
    public int getThisHourData(LocalDateTime date) {
        List<HourlyProduction> hourlyProductions = hourlyProductionRepository.findByTimestampBetween(
            date.withMinute(0).withSecond(0).withNano(0),
            date.withMinute(59).withSecond(59).withNano(999999999)
        );
        int totalQuantity = hourlyProductions.stream().mapToInt(HourlyProduction::getQuantity).sum();
        return totalQuantity;
    }
    

    // Method to fetch data for the current day
    public int getThisDayData(LocalDateTime date) {
        Optional<DailyProduction> dailyProduction = dailyProductionRepository.findByDate(date.toLocalDate());
        return dailyProduction.isPresent() ? dailyProduction.get().getQuantity() : 50;
    }

    // Method to fetch hourly production data for a specific date
    public List<HourlyProduction> thisDayDataByHour(LocalDate date) {
        return hourlyProductionRepository.findByTimestampBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }
    // Method to fetch hourly production data for btwn specific dates
    public List<DailyProduction> getDailyDataBetween(LocalDate startDate, LocalDate endDate) {
        return dailyProductionRepository.findByDateBetween(startDate, endDate);
    }
}
