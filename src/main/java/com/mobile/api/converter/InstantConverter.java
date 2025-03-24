package com.mobile.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class InstantConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT; // "yyyy-MM-dd'T'HH:mm:ss'Z'"

    @Component
    public static class StringToInstantConverter implements Converter<String, Instant> {
        @Override
        public Instant convert(String source) {
            if (source == null || source.isEmpty()) return null;
            return Instant.parse(source);
        }
    }

    @Component
    public static class InstantToStringConverter implements Converter<Instant, String> {
        @Override
        public String convert(Instant source) {
            return (source == null) ? null : FORMATTER.format(source.truncatedTo(ChronoUnit.SECONDS));
        }
    }
}
