package com.cube.springShowcase.config;

import javax.servlet.Filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class WebConfig {
	Logger logger = LogManager.getLogger(getClass());

	@Bean
	public Filter TracingFilter() {
		return new AWSXRayServletFilter("MyApp-TEST2");
	}
}