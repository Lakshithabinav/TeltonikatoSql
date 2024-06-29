package com.example.sampleSQLByTime.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.Entity.*;
import com.example.sampleSQLByTime.Repository.*;

/**
 * Service class for managing and fetching production data.
 */
@Service
public class ProductionDataService {
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

    /**
     * Method to fetch the last recorded minute production data.
     * 
     * @return a list of tuples containing the timestamp and data of the last recorded values.
     */
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

    /**
     * Method to fetch the production data for the current hour.
     * 
     * @param date the date and time used to fetch the production data
     * @return a list of integers representing the sum of production values for the current hour.
     */
    public List<Integer> getThisHourData(LocalDateTime date) {
        List<HourlyProduction> hourlyProductions = hourlyProductionRepository.findByTimestampBetween(
            date.withMinute(0).withSecond(0).withNano(0),
            date.withMinute(59).withSecond(59).withNano(999999999)
        );
        return List.of(hourlyProductions.stream().mapToInt(HourlyProduction::getFstValue).sum(),
                       hourlyProductions.stream().mapToInt(HourlyProduction::getSecondValue).sum(),
                       hourlyProductions.stream().mapToInt(HourlyProduction::getThirdValue).sum());
    }

    /**
     * Method to fetch the production data for the current day.
     * 
     * @param date the date used to fetch the production data
     * @return a list of integers representing the production values for the current day.
     */
    public List<Integer> getThisDayData(LocalDateTime date) {
        Optional<DailyProduction> dailyProduction = dailyProductionRepository.findByDate(date.toLocalDate());
        return dailyProduction.isPresent() ? 
            List.of(dailyProduction.get().getFstValue(), 
                    dailyProduction.get().getSecondValue(), 
                    dailyProduction.get().getThirdValue()) : 
            null;
    }

    /**
     * Method to fetch the hourly production data for a specific date.
     * 
     * @param date the date used to fetch the hourly production data
     * @return a list of HourlyProduction objects representing the production data for each hour of the specified date.
     */
    public List<HourlyProduction> thisDayDataByHour(LocalDate date) {
        return hourlyProductionRepository.findByTimestampBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }

    /**
     * Method to fetch the daily production data between specific dates.
     * 
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of DailyProduction objects representing the production data between the specified dates.
     */
    public List<DailyProduction> getDailyDataBetween(LocalDate startDate, LocalDate endDate) {
        return dailyProductionRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * A simple tuple class to hold the timestamp and data values.
     */
    public static class Tuple {
        private LocalDateTime timestamp;
        private int data;

        /**
         * Constructor to initialize the timestamp and data.
         * 
         * @param timestamp the timestamp of the production data
         * @param data the production data value
         */
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
