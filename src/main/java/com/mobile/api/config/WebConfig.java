package com.mobile.api.config;

import com.mobile.api.converter.InstantConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new InstantConverter.StringToInstantConverter());
        registry.addConverter(new InstantConverter.InstantToStringConverter());
    }
}
