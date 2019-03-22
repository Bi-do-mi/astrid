package com.bidomi.astrid.Converters;

import com.fasterxml.jackson.databind.util.StdConverter;

public class PasswordToNull extends StdConverter<String, String> {

    @Override
    public String convert(String s) {
        return null;
    }
}
