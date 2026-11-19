package com.dataset.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonValueComparatorTest {

    private final JsonValueComparator comparator =
            new JsonValueComparator();

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Test
    void shouldCompareNumbersCorrectly() throws Exception {

        JsonNode first = objectMapper.readTree("25");
        JsonNode second = objectMapper.readTree("30");

        assertTrue(comparator.compare(first, second) < 0);
        assertTrue(comparator.compare(second, first) > 0);
    }

    @Test
    void shouldCompareStringsIgnoringCase() throws Exception {

        JsonNode first = objectMapper.readTree("\"Amit\"");
        JsonNode second = objectMapper.readTree("\"rahul\"");

        assertTrue(comparator.compare(first, second) < 0);
    }

    @Test
    void shouldCompareBooleansCorrectly() throws Exception {

        JsonNode first = objectMapper.readTree("false");
        JsonNode second = objectMapper.readTree("true");

        assertTrue(comparator.compare(first, second) < 0);
    }

    @Test
    void shouldPlaceNullValuesLast() throws Exception {

        JsonNode value = objectMapper.readTree("25");

        assertTrue(comparator.compare(null, value) > 0);
        assertTrue(comparator.compare(value, null) < 0);
        assertEquals(0, comparator.compare(null, null));
    }

    @Test
    void shouldRejectArrayValues() throws Exception {

        JsonNode first = objectMapper.readTree("[1, 2]");

        assertThrows(
                IllegalArgumentException.class,
                () -> comparator.compare(first, first)
        );
    }

    @Test
    void shouldRejectObjectValues() throws Exception {

        JsonNode first = objectMapper.readTree(
                "{\"name\":\"Amit\"}");

        assertThrows(
                IllegalArgumentException.class,
                () -> comparator.compare(first, first)
        );
    }
}
