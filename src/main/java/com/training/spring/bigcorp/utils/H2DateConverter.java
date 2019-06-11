package com.training.spring.bigcorp.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Service
public class H2DateConverter implements Converter<String, Instant> {

    private static SimpleDateFormat H2_INSTANT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");

    @Override
    public Instant convert(String source) {
        try {
            return H2_INSTANT_FORMAT.parse(source).toInstant();
        } catch (ParseException e) {
            return null;
        }


    }
}
