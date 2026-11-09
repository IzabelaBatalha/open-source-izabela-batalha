package com.workflex.workation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "workflex.csv-import")
@Getter
@Setter
public class CsvImportProperties {

    private boolean enabled = true;
    private String resourcePath = "classpath:data/workations.csv";
}
