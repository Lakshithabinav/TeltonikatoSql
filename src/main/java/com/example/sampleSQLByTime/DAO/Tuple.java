package com.example.sampleSQLByTime.DAO;

import java.time.LocalDateTime;

public class Tuple {
    private LocalDateTime timestamp;
    private int data;
    private int hourData;
    private int dailyData;

    /**
     * Constructor to initialize the timestamp and data.
     * 
     * @param timestamp the timestamp of the production data
     * @param data the production data value
     * @param hourData the hourly production data value
     * @param dailyData the daily production data value
     */
    public Tuple(LocalDateTime timestamp, int data, int hourData, int dailyData) {
        this.timestamp = timestamp;
        this.data = data;
        this.hourData = hourData;
        this.dailyData = dailyData;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getHourData() {
        return hourData;
    }

    public void setHourData(int hourData) {
        this.hourData = hourData;
    }

    public int getDailyData() {
        return dailyData;
    }

    public void setDailyData(int dailyData) {
        this.dailyData = dailyData;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "timestamp=" + timestamp +
                ", data=" + data +
                ", hourData=" + hourData +
                ", dailyData=" + dailyData +
                '}';
    }
}
