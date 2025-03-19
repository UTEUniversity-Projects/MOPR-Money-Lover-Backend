package com.mobile.api.config;

import com.mobile.api.converter.LocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateTimeConverter.StringToLocalDateTimeConverter());
        registry.addConverter(new LocalDateTimeConverter.LocalDateTimeToStringConverter());
    }
}
