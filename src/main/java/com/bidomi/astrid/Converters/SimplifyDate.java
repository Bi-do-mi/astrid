package com.bidomi.astrid.Converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.joda.time.DateTime;

public class SimplifyDate extends StdConverter<DateTime, String> {
    @Override
    public String convert(DateTime date) {
        return date.toString();
    }
}
