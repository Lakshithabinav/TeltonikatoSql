package com.example.sampleSQLByTime.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataModel {
    private int data;
    private String name;
    private String time; // Keep the original time string for reference
    private LocalDateTime timestamp; // Store the formatted date as LocalDateTime
    
    // Getters and setters...

    public int getData() {
        return data;
    }

    public void setData(String data) {
        // Remove brackets and parse the integer value
        this.data = Integer.parseInt(data.replaceAll("[\\[\\]]", ""));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime formattedDate) {
        this.timestamp = formattedDate;
    }

    // Additional method to set timestamp from formatted string
    public void setTimestamp(String formattedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.timestamp = LocalDateTime.parse(formattedDate, formatter);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "data=" + data +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
