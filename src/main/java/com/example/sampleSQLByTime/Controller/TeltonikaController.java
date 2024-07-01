package com.example.sampleSQLByTime.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sampleSQLByTime.DAO.ProductionDataSummary;
import com.example.sampleSQLByTime.Entity.DailyProduction;
import com.example.sampleSQLByTime.Entity.HourlyProduction;
import com.example.sampleSQLByTime.Service.InputDataProcessor;
import com.example.sampleSQLByTime.Service.ProductionDataService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
public class TeltonikaController {

    private final ProductionDataSummary productionDataSummary;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProductionDataService productionDataService;
    private final InputDataProcessor inputDataProcessor;

    // Constructor injection for dependencies
    public TeltonikaController(ProductionDataSummary productionDataSummary, SimpMessagingTemplate messagingTemplate, ProductionDataService dataTotalService, InputDataProcessor inputDataProcessor) {
        this.productionDataSummary = productionDataSummary;
        this.messagingTemplate = messagingTemplate;
        this.productionDataService = dataTotalService;
        this.inputDataProcessor = inputDataProcessor;
    }

    // Simple GET endpoint to check if the service is running
    @GetMapping("/")
    public String data() {
        return "hello world";
    }

    // GET endpoint to fetch data and return the ProductionDataSummary object
    @GetMapping("/data")
    public ResponseEntity<ProductionDataSummary> getData() {
        System.out.println("helooooooooooooooooooooooooooooooooooo");
        // Fetch data based on the current time
        productionDataSummary.fetchDataByTime(LocalDateTime.now());
        return ResponseEntity.ok(productionDataSummary);
    }

    // POST endpoint to receive encoded data, process it, and return the DataModelOut object
    @PostMapping("/data")
    public ResponseEntity<ProductionDataSummary> receiveData(@RequestBody String encodedData) {
        try {
            // Decode and process the input data
            inputDataProcessor.inputDataEncode(encodedData, productionDataSummary);
            // Send the processed data to the WebSocket topic "/topic/dataByTime"
            messagingTemplate.convertAndSend("/topic/dataByTime", productionDataSummary);
            return ResponseEntity.ok(productionDataSummary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // GET endpoint to fetch daily production data between two dates
    @GetMapping("/get-data-btwn-dates")
    public List<DailyProduction> getDailyProductionBetween(@RequestParam("startDate") String startDate,
                                                            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return productionDataService.getDailyDataBetween(start, end);
    }

    // GET endpoint to fetch hourly production data for a specific date
    @GetMapping("/get-data-of-one-day")
    public List<HourlyProduction> getMethodName(@RequestParam("oneDay") String dateString) {
        LocalDate dataOfDate = LocalDate.parse(dateString);
        return productionDataService.thisDayDataByHour(dataOfDate);
    }
}
