package com.example.sampleSQLByTime.DAO;

import java.time.LocalDateTime;
import java.util.List;

import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Service.DataTotalService;



import org.springframework.stereotype.Component;

@Component
public class DataModelOut {

    private int hourData;
    private int todayData;
    private List<HourlyProduction> todaysTotalProductionbyHour;

    private final DataTotalService dataTotalService;

    public DataModelOut(DataTotalService dataTotalService) {
        this.dataTotalService = dataTotalService;
    }

    public void fetchDataByTime(LocalDateTime date) {
        
        this.hourData = dataTotalService.getThisHourData(date);
        this.todayData = dataTotalService.getThisDayData(date);
        this.todaysTotalProductionbyHour = dataTotalService.thisDayDataByHour(date.toLocalDate());
    }

  
    public int getHourData() {
        return hourData;
    }

    public int getTodayData() {
        return todayData;
    }

    public List<HourlyProduction> getTodaysTotalProductionbyHour() {
        return todaysTotalProductionbyHour;
    }
}
