package com.example.sampleSQLByTime.DAO;

import java.util.Date;

public class DataModel {
    private int data;
    private String name;
    private String time; // Keep the original time string for reference
    private String timestamp; // Store the formatted Date as string

    // Getters and setters
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "data=" + data +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
