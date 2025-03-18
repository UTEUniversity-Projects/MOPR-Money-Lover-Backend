package com.mobile.base.config;

import com.mobile.base.converter.LocalDateTimeConverter;
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
