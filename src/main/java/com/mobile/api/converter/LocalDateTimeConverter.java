package com.mobile.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Component
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            if (source == null || source.isEmpty()) return null;
            if (source.endsWith("Z")) {
                return OffsetDateTime.parse(source).atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            }
            return LocalDateTime.parse(source, FORMATTER);
        }
    }

    @Component
    public static class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(LocalDateTime source) {
            return (source == null) ? null : source.atZone(ZoneId.of("UTC")).format(FORMATTER);
        }
    }
}
