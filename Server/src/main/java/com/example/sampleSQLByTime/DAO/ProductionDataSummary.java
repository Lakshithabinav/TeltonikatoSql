package com.example.sampleSQLByTime.DAO;

import java.time.LocalDateTime;
import java.util.List;

import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Service.ProductionDataService;


import org.springframework.stereotype.Component;

/**
 * The ProductionDataSummary class is responsible for holding the summary of production data.
 * It contains data for the last recorded minute values, hourly production data, 
 * today's total production data, and hourly production details for the current day.
 */
@Component
public class ProductionDataSummary {
    private List<Tuple> data;  // List of tuples containing the last recorded minute values
    private List<HourlyProduction> todaysTotalProductionbyHour;  // Hourly production details for the current day

    private final ProductionDataService dataTotalService;

    /**
     * Constructor to initialize the DataTotalService dependency.
     * 
     * @param dataTotalService the service used to fetch production data
     */
    public ProductionDataSummary(ProductionDataService productionDataService) {
        this.dataTotalService = productionDataService;
    }

    /**
     * Fetches and updates the production data summary based on the given date and time.
     * 
     * @param date the date and time used to fetch the production data
     */
    public void fetchDataByTime(LocalDateTime date) {
        this.data = dataTotalService.getLastData(date);
        this.todaysTotalProductionbyHour = dataTotalService.thisDayDataByHour(date.toLocalDate());
    }

    /**
     * Returns the list of tuples containing the last recorded minute values.
     * 
     * @return the data list
     */
    public List<Tuple> getData() {
        return data;
    }

    /**
     * Returns the hourly production details for the current day.
     * 
     * @return the today's total production by hour list
     */
    public List<HourlyProduction> getTodaysTotalProductionbyHour() {
        return todaysTotalProductionbyHour;
    }
}
