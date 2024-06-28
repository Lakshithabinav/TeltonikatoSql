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


import com.example.sampleSQLByTime.DAO.DataModelOut;
import com.example.sampleSQLByTime.Entity.DailyProduction;
import com.example.sampleSQLByTime.Entity.HourlyProduction;

import com.example.sampleSQLByTime.Service.DataTotalService;
import com.example.sampleSQLByTime.Service.InputDataEncode;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
public class TeltonikaController {

    private final DataModelOut dataModelOut;
    private final SimpMessagingTemplate messagingTemplate;
    private final DataTotalService dataTotalService;
    private final InputDataEncode inputDataEncode;

    public TeltonikaController(DataModelOut dataByTime, SimpMessagingTemplate messagingTemplate, DataTotalService dataTotalService, InputDataEncode inputDataEncode) {
        this.dataModelOut = dataByTime;
        this.messagingTemplate = messagingTemplate;
        this.dataTotalService = dataTotalService;
        this.inputDataEncode = inputDataEncode;
    }

    @GetMapping("/")
    public String data() {
        return "hello world";
    }

    @GetMapping("/data")
    public ResponseEntity<DataModelOut> getMethodName() {
        dataModelOut.fetchDataByTime(LocalDateTime.now(), 0);
        return ResponseEntity.ok(dataModelOut);
    }

    @PostMapping("/data")
    public ResponseEntity<DataModelOut> receiveData(@RequestBody String encodedData) {
        try {
            inputDataEncode.inputDataEncode(encodedData);
            messagingTemplate.convertAndSend("/topic/dataByTime", dataModelOut);
            return ResponseEntity.ok(dataModelOut);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-data-btwn-dates")
    public List<DailyProduction> getDailyProductionBetween(@RequestParam("startDate") String startDate,
                                                           @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return dataTotalService.getDailyDataBetween(start, end);
    }

    @GetMapping("/get-data-of-one-day")
    public List<HourlyProduction> getMethodName(@RequestParam("oneDay") String dateString) {
        LocalDate dataOfDate = LocalDate.parse(dateString);
        return dataTotalService.thisDayDataByHour(dataOfDate);
    }
}
