package com.example.sampleSQLByTime.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.Entity.*;
import com.example.sampleSQLByTime.Repository.*;

@Service
public class DataTotalService {
    @Autowired
    private HourlyProductionRepository hourlyProductionRepository;
    @Autowired
    private DailyProductionRepository dailyProductionRepository;
    @Autowired
    private MinuteProductionOfFirstValueRepository firstValueRepository;
    @Autowired
    private MinuteProductionOfSecondValueRepository secondValueRepository;
    @Autowired
    private MinuteProductionOfThirdValueRepository thirdValueRepository;

    // Method to fetch last recorded data of minutes
    public List<Tuple> getLastData() {
        MinuteProductionOfFirstValue lastFirstValue = firstValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfSecondValue lastSecondValue = secondValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfThirdValue lastThirdValue = thirdValueRepository.findTopByOrderByTimestampDesc();

        return List.of(
            new Tuple(lastFirstValue != null ? lastFirstValue.getTimestamp() : null, lastFirstValue != null ? lastFirstValue.getData() : 0),
            new Tuple(lastSecondValue != null ? lastSecondValue.getTimestamp() : null, lastSecondValue != null ? lastSecondValue.getData() : 0),
            new Tuple(lastThirdValue != null ? lastThirdValue.getTimestamp() : null, lastThirdValue != null ? lastThirdValue.getData() : 0)
        );
    }

    // Method to fetch data for the current hour
    public int getThisHourData(LocalDateTime date) {
        List<HourlyProduction> hourlyProductions = hourlyProductionRepository.findByTimestampBetween(
            date.withMinute(0).withSecond(0).withNano(0),
            date.withMinute(59).withSecond(59).withNano(999999999)
        );
        return hourlyProductions.stream().mapToInt(HourlyProduction::getFstValue).sum()
             + hourlyProductions.stream().mapToInt(HourlyProduction::getSecondValue).sum()
             + hourlyProductions.stream().mapToInt(HourlyProduction::getThirdValue).sum();
    }

    // Method to fetch data for the current day
    public int getThisDayData(LocalDateTime date) {
        Optional<DailyProduction> dailyProduction = dailyProductionRepository.findByDate(date.toLocalDate());
        return dailyProduction.isPresent() ? dailyProduction.get().getFstValue()
                + dailyProduction.get().getSecondValue()
                + dailyProduction.get().getThirdValue() : 50;
    }

    // Method to fetch hourly production data for a specific date
    public List<HourlyProduction> thisDayDataByHour(LocalDate date) {
        return hourlyProductionRepository.findByTimestampBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }

    // Method to fetch daily production data between specific dates
    public List<DailyProduction> getDailyDataBetween(LocalDate startDate, LocalDate endDate) {
        return dailyProductionRepository.findByDateBetween(startDate, endDate);
    }

    // Define a simple tuple class to hold the date and integer values
    public static class Tuple {
        private LocalDateTime timestamp;
        private int data;

        public Tuple(LocalDateTime timestamp, int data) {
            this.timestamp = timestamp;
            this.data = data;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public int getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "timestamp=" + timestamp +
                    ", data=" + data +
                    '}';
        }
    }
}
