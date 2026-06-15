package com.company.physical_activity_log_api.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

	private List<String> allowedOriginPatterns = List.of("*");

}
