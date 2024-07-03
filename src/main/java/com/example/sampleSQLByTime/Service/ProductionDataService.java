package com.example.sampleSQLByTime.Service;

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
import com.example.sampleSQLByTime.DAO.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public List<Tuple> getLastData(LocalDateTime date) {
        MinuteProductionOfFirstValue lastFirstValue = firstValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfSecondValue lastSecondValue = secondValueRepository.findTopByOrderByTimestampDesc();
        MinuteProductionOfThirdValue lastThirdValue = thirdValueRepository.findTopByOrderByTimestampDesc();
        GetThisHourData getThisHourData = new GetThisHourData(date);
        GetThisDayData getThisDayData = new GetThisDayData(date);
        return List.of(
            new Tuple(
                "First Value",
                lastFirstValue != null ? lastFirstValue.getTimestamp() : null,
                lastFirstValue != null ? lastFirstValue.getData() : 0,
                getThisHourData.SpecificDataValue.getFirstData(),
                getThisDayData.SpecificDataValue.getFirstData()
            ),
            new Tuple(
                "Second Value",
                lastSecondValue != null ? lastSecondValue.getTimestamp() : null,
                lastSecondValue != null ? lastSecondValue.getData() : 0,
                getThisHourData.SpecificDataValue.getSecondData(),
                getThisDayData.SpecificDataValue.getSecondData()
            ),
            new Tuple(
                "Third Value",
                lastThirdValue != null ? lastThirdValue.getTimestamp() : null,
                lastThirdValue != null ? lastThirdValue.getData() : 0,
                getThisHourData.SpecificDataValue.getThirdData(),
                getThisDayData.SpecificDataValue.getThirdData()
            )
        );
    }

    public List<HourlyProduction> thisDayDataByHour(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);
        return hourlyProductionRepository.findByTimestampBetween(startOfDay, endOfDay);
    }

    public List<DailyProduction> getDailyDataBetween(LocalDate startDate, LocalDate endDate) {
        return dailyProductionRepository.findByDateBetween(startDate, endDate);
    }

    public class GetThisHourData {
        List<HourlyProduction> hourlyProductions;

        GetThisHourData(LocalDateTime date) {
            LocalDateTime start = date.withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = date.withMinute(59).withSecond(59).withNano(999999999);
            this.hourlyProductions = hourlyProductionRepository.findByTimestampBetween(start, end);
        }

        SpecificDataValue SpecificDataValue = new SpecificDataValue();

        class SpecificDataValue {
            public int getFirstData() {
                return hourlyProductions.isEmpty() ? 0 : hourlyProductions.stream().mapToInt(HourlyProduction::getFstValue).sum();
            }

            public int getSecondData() {
                return hourlyProductions.isEmpty() ? 0 : hourlyProductions.stream().mapToInt(HourlyProduction::getSecondValue).sum();
            }

            public int getThirdData() {
                return hourlyProductions.isEmpty() ? 0 : hourlyProductions.stream().mapToInt(HourlyProduction::getThirdValue).sum();
            }
        }
    }

    public class GetThisDayData {
        Optional<DailyProduction> dailyProduction;

        GetThisDayData(LocalDateTime date) {
            this.dailyProduction = dailyProductionRepository.findByDate(date.toLocalDate());
        }

        SpecificDataValue SpecificDataValue = new SpecificDataValue();

        class SpecificDataValue {
            public int getFirstData() {
                return dailyProduction.isPresent() ? dailyProduction.get().getFstValue() : 0;
            }

            public int getSecondData() {
                return dailyProduction.isPresent() ? dailyProduction.get().getSecondValue() : 0;
            }

            public int getThirdData() {
                return dailyProduction.isPresent() ? dailyProduction.get().getThirdValue() : 0;
            }
        }
    }
}
