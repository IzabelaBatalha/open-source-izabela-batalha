package com.workflex.workation;

import com.workflex.workation.config.CsvImportProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CsvImportProperties.class)
public class WorkationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkationApplication.class, args);
    }
}
