package com.example.sampleSQLByTime.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.sampleSQLByTime.DAO.DataModel;
import com.example.sampleSQLByTime.DAO.ProductionDataSummary;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfFirstValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfSecondValue;
import com.example.sampleSQLByTime.Entity.MinuteProductionOfThirdValue;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InputDataProcessor {

    private final ObjectMapper objectMapper;
    private final ProductionService productionService;
    

   
    public InputDataProcessor(ObjectMapper objectMapper, ProductionService productionService) {
        this.objectMapper = objectMapper;
        this.productionService = productionService;
       
    }

    public void inputDataEncode(String encodedData, ProductionDataSummary productionDataSummary) {
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
                    minuteProduction.setTimestamp(dataModel.getTimestamp());
                    productionService.saveMinuteProductionOfFirstValue(minuteProduction);
                } 
                else if (dataModel.getName().equals("SecondValue")) {
                    MinuteProductionOfSecondValue minuteProduction = new MinuteProductionOfSecondValue();
                    minuteProduction.setData(dataModel.getData());
                    minuteProduction.setTimestamp(dataModel.getTimestamp());
                    productionService.saveMinuteProductionOfSecondValue(minuteProduction);
                }
                 else if (dataModel.getName().equals("ThirdValue")) {
                    MinuteProductionOfThirdValue minuteProduction = new MinuteProductionOfThirdValue();
                    minuteProduction.setData(dataModel.getData());
                    minuteProduction.setTimestamp(dataModel.getTimestamp());
                    productionService.saveMinuteProductionOfThirdValue(minuteProduction);
                }
            }
            productionDataSummary.fetchDataByTime(dataModels[0].getTimestamp());
        } catch (Exception e) {
            System.out.println("Error processing data: " + e.getMessage());
        }
    }
}
