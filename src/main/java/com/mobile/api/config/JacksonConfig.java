package com.mobile.api.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.api.component.CustomDoubleSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Double.class, new CustomDoubleSerializer());
        module.addSerializer(Double.TYPE, new CustomDoubleSerializer());
        mapper.registerModule(module);
        return mapper;
    }
}
