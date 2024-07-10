package com.example.sampleSQLByTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.SpringServletContainerInitializer;

@SpringBootApplication
public class SampleSqlByTimeApplication extends SpringServletContainerInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SampleSqlByTimeApplication.class, args);
	}

}
