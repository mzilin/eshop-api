package com.mariuszilinskas.eshopapi.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AppUtils {

    private AppUtils() {
        // Private constructor to prevent instantiation
    }

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static List<String> convertEnumsToStrings(List<? extends Enum<?>> enumValues) {
        if (enumValues == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        for (Enum<?> enumValue : enumValues) {
            result.add(enumValue.name().toLowerCase());
        }
        return result;
    }

    public static <E extends Enum<E>> List<E> convertStringsToEnums(List<String> values, Class<E> enumClass) {
        if (values == null || enumClass == null) {
            return Collections.emptyList();
        }

        List<E> enums = new ArrayList<>();
        for (String value : values) {
            try {
                enums.add(Enum.valueOf(enumClass, value.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("There was an error converting '" + value + "' to " + enumClass.getSimpleName());
            }
        }
        return enums;
    }

    public static String convertToDate(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return zonedDateTime.format(formatter);
    }

}
