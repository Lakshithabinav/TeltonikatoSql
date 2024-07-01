package com.example.sampleSQLByTime.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.DAO.Tuple;
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
    public List<Tuple> getLastData(LocalDateTime date) {
        MinuteProductionOfFirstValue lastFirstValue = firstValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfSecondValue lastSecondValue = secondValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfThirdValue lastThirdValue = thirdValueRepository.findTopByOrderByTimestampDesc();
        GetThisHourData getThisHourData = new GetThisHourData(date);
        GetThisDayData getThisDayData = new GetThisDayData(date);
        return List.of(
            new Tuple(lastFirstValue != null ? lastFirstValue.getTimestamp() : null, lastFirstValue != null ? lastFirstValue.getData() : 0, getThisHourData.SpecificDataValue.getFirstData(), getThisDayData.SpecificDataValue.getFirstData()),
            new Tuple(lastSecondValue != null ? lastSecondValue.getTimestamp() : null, lastSecondValue != null ? lastSecondValue.getData() : 0, getThisHourData.SpecificDataValue.getSecondData(), getThisDayData.SpecificDataValue.getSecondData()),
            new Tuple(lastThirdValue != null ? lastThirdValue.getTimestamp() : null, lastThirdValue != null ? lastThirdValue.getData() : 0, getThisHourData.SpecificDataValue.getThirdData(), getThisDayData.SpecificDataValue.getThirdData())
        );
    }

    

    /**
     * Method to fetch the production data for the current hour.
     * 
     * @param date the date and time used to fetch the production data
     * @return a list of integers representing the sum of production values for the current hour.
     */
    public class GetThisHourData {
        public List<HourlyProduction> hourlyProductions;

        GetThisHourData(LocalDateTime date){
        this.hourlyProductions = hourlyProductionRepository.findByTimestampBetween(
            date.withMinute(0).withSecond(0).withNano(0),
            date.withMinute(59).withSecond(59).withNano(999999999)
        );
        }
        SpecificDataValue  SpecificDataValue = new SpecificDataValue();
        class SpecificDataValue{            
            public int getFirstData(){
                return hourlyProductions.stream().mapToInt(HourlyProduction::getFstValue).sum();
            }
            public int getSecondData(){
                return hourlyProductions.stream().mapToInt(HourlyProduction::getSecondValue).sum();
            }
            public int getThirdData(){
                return hourlyProductions.stream().mapToInt(HourlyProduction::getThirdValue).sum();
            }
        };
    }

    /**
     * Method to fetch the production data for the current day.
     * 
     * @param date the date used to fetch the production data
     * @return a list of integers representing the production values for the current day.
     */
    class GetThisDayData {
        Optional<DailyProduction> dailyProduction;
        GetThisDayData(LocalDateTime date){
            this.dailyProduction = dailyProductionRepository.findByDate(date.toLocalDate());
        }
        SpecificDataValue  SpecificDataValue = new SpecificDataValue();
        class SpecificDataValue{            
            public int getFirstData(){
                return dailyProduction.get().getFstValue();
            }
            public int getSecondData(){
                return dailyProduction.get().getSecondValue();
            }
            public int getThirdData(){
                return dailyProduction.get().getThirdValue();
            }
        };
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


}