package com.github.thresno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThresholdNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThresholdNotifierApplication.class, args);
    }
}
