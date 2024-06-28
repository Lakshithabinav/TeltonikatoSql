package com.example.sampleSQLByTime.DAO;

import java.time.LocalDateTime;
import java.util.List;

import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Service.DataTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataModelOut {
    private int data;
    private int hourData;
    private int todayData;
    private List<HourlyProduction> todaysTotalProductionbyHour;

    private final DataTotalService dataTotalService;

    @Autowired
    public DataModelOut(DataTotalService dataTotalService) {
        this.dataTotalService = dataTotalService;
    }

    public void fetchDataByTime(LocalDateTime date,int data) {
        this.data = data;
        this.hourData = dataTotalService.getThisHourData(date);
        this.todayData = dataTotalService.getThisDayData(date);
        this.todaysTotalProductionbyHour = dataTotalService.thisDayDataByHour(date.toLocalDate());
    }
    public int getData(){
        return data;
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
