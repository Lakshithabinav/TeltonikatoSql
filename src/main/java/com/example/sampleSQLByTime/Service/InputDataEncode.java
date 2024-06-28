package com.example.sampleSQLByTime.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.DAO.DataModelOut;
import com.example.sampleSQLByTime.DAO.DataModel;
import com.example.sampleSQLByTime.DAO.DataModelOut;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfFirstValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfSecondValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfThirdValue;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InputDataEncode {

    private final ObjectMapper objectMapper;
    private final ProductionService productionService;
    private final DataModelOut dataModelOut;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public InputDataEncode(ObjectMapper objectMapper, ProductionService productionService, DataModelOut dataByTime, SimpMessagingTemplate messagingTemplate) {
        this.objectMapper = objectMapper;
        this.productionService = productionService;
        this.dataModelOut = dataByTime;
        this.messagingTemplate = messagingTemplate;
    }

    public void inputDataEncode(String encodedData) {
        try {
            System.out.println("Encoded data: " + encodedData);

            // Decode the URL-encoded data
            String decodedData = URLDecoder.decode(encodedData, StandardCharsets.UTF_8.toString());
            System.out.println("Decoded data: " + decodedData);

            // Parse the JSON data
            DataModel[] dataModels = objectMapper.readValue(decodedData, DataModel[].class);

            // Define the input date format
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            // Define the output date format
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

            // Process the received data
            System.out.println("Data Received");

            for (DataModel dataModel : dataModels) {
                // Parse the time string to a Date object
                try {
                    Date parsedDate = inputDateFormat.parse(dataModel.getTime());
                    String formattedDate = outputDateFormat.format(parsedDate);
                    dataModel.setTimestamp(formattedDate);
                    dataModel.setTime(formattedDate);
                } catch (ParseException e) {
                    System.out.println("Error parsing date: " + e.getMessage());
                }

                System.out.println(dataModel.getName() + ": " + dataModel.getData() + ", " + dataModel.getTimestamp());
                if (dataModel.getName().equals("FirstValue")) {
                    MinuteProductionOfFirstValue minuteProduction = new MinuteProductionOfFirstValue();
                    minuteProduction.setData(dataModel.getData());
                    minuteProduction.setTimestamp(LocalDateTime.now());
                    productionService.saveMinuteProductionOfFirstValue(minuteProduction);
                } else if (dataModel.getName().equals("SecondValue")) {
                    MinuteProductionOfSecondValue minuteProduction = new MinuteProductionOfSecondValue();
                    minuteProduction.setData(dataModel.getData());
                    minuteProduction.setTimestamp(LocalDateTime.now());
                    productionService.saveMinuteProductionOfSecondValue(minuteProduction);
                } else if (dataModel.getName().equals("ThirdValue")) {
                    MinuteProductionOfThirdValue minuteProduction = new MinuteProductionOfThirdValue();
                    minuteProduction.setData(dataModel.getData());
                    minuteProduction.setTimestamp(LocalDateTime.now());
                    productionService.saveMinuteProductionOfThirdValue(minuteProduction);
                }
            }

            // Fetch data by time
            dataModelOut.fetchDataByTime(LocalDateTime.now(), dataModels[0].getData());

        } catch (Exception e) {
            System.out.println("Error processing data: " + e.getMessage());
        }
    }
}
