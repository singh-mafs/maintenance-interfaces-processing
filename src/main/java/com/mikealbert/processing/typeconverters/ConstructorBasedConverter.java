package com.mikealbert.processing.typeconverters;

import java.lang.reflect.Constructor;

import org.apache.camel.Converter;
import org.apache.camel.FallbackConverter;
import org.apache.camel.spi.TypeConverterRegistry;

@Converter
public class ConstructorBasedConverter {
    @SuppressWarnings("unchecked")
    @FallbackConverter
    public static <T> T convertTo(Class<T> type, Object value, TypeConverterRegistry registry) throws Throwable {
        for (Constructor<?> c : type.getConstructors()) {
            Class<?>[] types = c.getParameterTypes();
            if (types.length == 1 && types[0].isAssignableFrom(value.getClass())) {
                    return (T) c.newInstance(value);
            }
        }
        return null;
    }
}