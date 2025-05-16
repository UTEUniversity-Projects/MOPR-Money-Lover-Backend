package com.mobile.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mobile.api.component.CustomBigDecimalSerializer;
import com.mobile.api.component.CustomDoubleSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create and configure module for numeric serializers
        SimpleModule numericModule = new SimpleModule();

        // Add Double serializer
        numericModule.addSerializer(Double.class, new CustomDoubleSerializer());
        numericModule.addSerializer(Double.TYPE, new CustomDoubleSerializer());

        // Add BigDecimal serializer
        numericModule.addSerializer(BigDecimal.class, new CustomBigDecimalSerializer());

        // Register the numeric serializers module
        objectMapper.registerModule(numericModule);

        // Register JavaTimeModule for Java date/time types
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Disable serialization of empty collections
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }
}