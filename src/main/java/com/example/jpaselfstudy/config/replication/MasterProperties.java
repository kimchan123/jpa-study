package com.example.jpaselfstudy.config.replication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.write")
public class MasterProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
