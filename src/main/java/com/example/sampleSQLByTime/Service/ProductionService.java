package com.example.sampleSQLByTime.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.Entity.DailyProduction;
import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfFirstValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfSecondValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfThirdValue;
import com.example.sampleSQLByTime.Repository.DailyProductionRepository;
import com.example.sampleSQLByTime.Repository.HourlyProductionRepository;
import com.example.sampleSQLByTime.Repository.MinuteProductionOfFirstValueRepository;
import com.example.sampleSQLByTime.Repository.MinuteProductionOfSecondValueRepository;
import com.example.sampleSQLByTime.Repository.MinuteProductionOfThirdValueRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class ProductionService {

    @Autowired
    private MinuteProductionOfFirstValueRepository minuteProductionOfFirstValueRepository;
    @Autowired
    private MinuteProductionOfSecondValueRepository minuteProductionOfSecondValueRepository;
    @Autowired
    private MinuteProductionOfThirdValueRepository minuteProductionOfThirdValueRepository;
    @Autowired
    private HourlyProductionRepository hourlyProductionRepository;

    @Autowired
    private DailyProductionRepository dailyProductionRepository;

   

    public void saveMinuteProductionOfFirstValue(MinuteProductionOfFirstValue minuteProduction) {
        minuteProductionOfFirstValueRepository.save(minuteProduction);
        aggregateHourlyProduction(minuteProduction.getTimestamp().withMinute(0).withSecond(0).withNano(0));
        aggregateDailyProduction(minuteProduction.getTimestamp().toLocalDate());
        
    }

    public void saveMinuteProductionOfSecondValue(MinuteProductionOfSecondValue minuteProduction) {
        minuteProductionOfSecondValueRepository.save(minuteProduction);
        aggregateHourlyProduction(minuteProduction.getTimestamp().withMinute(0).withSecond(0).withNano(0));
        aggregateDailyProduction(minuteProduction.getTimestamp().toLocalDate());
    
    }

    public void saveMinuteProductionOfThirdValue(MinuteProductionOfThirdValue minuteProduction) {
        minuteProductionOfThirdValueRepository.save(minuteProduction);
        aggregateHourlyProduction(minuteProduction.getTimestamp().withMinute(0).withSecond(0).withNano(0));
        aggregateDailyProduction(minuteProduction.getTimestamp().toLocalDate());
        
    }

    private void aggregateHourlyProduction(LocalDateTime hour) {
        List<MinuteProductionOfFirstValue> minuteDataOfFirstValue = minuteProductionOfFirstValueRepository.findByTimestampBetween(hour, hour.plusHours(1));
        System.out.println("fetchedfirst");
        List<MinuteProductionOfSecondValue> minuteDataOfSecondValue = minuteProductionOfSecondValueRepository.findByTimestampBetween(hour, hour.plusHours(1));
        System.out.println("fetchedsec");
        List<MinuteProductionOfThirdValue> minuteDataOfThirdValue = minuteProductionOfThirdValueRepository.findByTimestampBetween(hour, hour.plusHours(1));
        System.out.println("fetchedthrd");

        int totalFirstValue = minuteDataOfFirstValue.stream().mapToInt(MinuteProductionOfFirstValue::getData).sum();
        int totalSecondValue = minuteDataOfSecondValue.stream().mapToInt(MinuteProductionOfSecondValue::getData).sum();
        int totalThirdValue = minuteDataOfThirdValue.stream().mapToInt(MinuteProductionOfThirdValue::getData).sum();

        List<HourlyProduction> hourlyProductions = hourlyProductionRepository.findByTimestamp(hour);
        HourlyProduction hourlyProduction;

        if (hourlyProductions.isEmpty()) {
            hourlyProduction = new HourlyProduction();
        } else {
            // Handle cases where multiple records are found
            hourlyProduction = hourlyProductions.get(0); // Adjust this logic as needed
        }
        hourlyProduction.setFstValue(totalFirstValue);
        hourlyProduction.setSecondValue(totalSecondValue);
        hourlyProduction.setThirdValue(totalThirdValue);
        hourlyProduction.setTimestamp(hour);

        hourlyProductionRepository.save(hourlyProduction);
    }

    private void aggregateDailyProduction(LocalDate date) {
        List<HourlyProduction> hourlyData = hourlyProductionRepository.findByTimestampBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        int totalFirstValue = hourlyData.stream().mapToInt(HourlyProduction::getFstValue).sum();
        int totalSecondValue = hourlyData.stream().mapToInt(HourlyProduction::getSecondValue).sum();
        int totalThirdValue = hourlyData.stream().mapToInt(HourlyProduction::getThirdValue).sum();
        DailyProduction dailyProduction = dailyProductionRepository.findByDate(date)
        .orElse(new DailyProduction());


        dailyProduction.setFstValue(totalFirstValue);
        dailyProduction.setSecondValue(totalSecondValue);
        dailyProduction.setThirdValue(totalThirdValue);
        dailyProduction.setDate(date);

        dailyProductionRepository.save(dailyProduction);
    }

}
