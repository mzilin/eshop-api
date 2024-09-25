package com.mariuszilinskas.eshopapi.util;

import com.mariuszilinskas.eshopapi.enums.Label;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppUtilsTest {

    @Test
    void testConvertEnumsToStrings() {
        // Arrange
        List<Label> enumValues = List.of(Label.DRINK, Label.FOOD, Label.CLOTHES);

        // Act
        List<String> result = AppUtils.convertEnumsToStrings(enumValues);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("drink", result.get(0));
        assertEquals("food", result.get(1));
        assertEquals("clothes", result.get(2));
    }

    @Test
    void testConvertEnumsToStrings_NullInput() {
        // Act
        List<String> result = AppUtils.convertEnumsToStrings(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ------------------------------------

    @Test
    void testConvertStringsToEnums() {
        // Arrange
        List<String> stringValues = List.of("drink", "food", "clothes");

        // Act
        List<Label> result = AppUtils.convertStringsToEnums(stringValues, Label.class);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Label.DRINK, result.get(0));
        assertEquals(Label.FOOD, result.get(1));
        assertEquals(Label.CLOTHES, result.get(2));
    }

    @Test
    void testConvertStringsToEnums_InvalidValue() {
        // Arrange
        List<String> stringValues = List.of("drink", "invalid", "food");

        // Act
        List<Label> result = AppUtils.convertStringsToEnums(stringValues, Label.class);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Label.DRINK, result.get(0));
        assertEquals(Label.FOOD, result.get(1));
    }

    @Test
    void testConvertStringsToEnums_NullInput() {
        // Act
        List<Label> result = AppUtils.convertStringsToEnums(null, Label.class);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ------------------------------------

    @Test
    void testConvertToDate() {
        // Arrange
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String expected = dateTime.format(formatter);

        // Act
        String result = AppUtils.convertToDate(dateTime);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void testConvertToDate_NullInput() {
        // Act
        String result = AppUtils.convertToDate(null);

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

}
