package com.dataset.api.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.Comparator;

public class JsonValueComparator implements Comparator<JsonNode> {

    @Override
    public int compare(JsonNode first, JsonNode second) {

        boolean firstIsNull = first == null || first.isNull();
        boolean secondIsNull = second == null || second.isNull();

        // Null and missing values should always appear last.
        if (firstIsNull && secondIsNull) {
            return 0;
        }

        if (firstIsNull) {
            return 1;
        }

        if (secondIsNull) {
            return -1;
        }

        // Validate both values before comparing.
        validateSortableValue(first);
        validateSortableValue(second);

        int firstType = getTypeOrder(first);
        int secondType = getTypeOrder(second);

        // Mixed type ordering: Number < String < Boolean.
        if (firstType != secondType) {
            return Integer.compare(firstType, secondType);
        }

        // Numeric comparison.
        if (first.isNumber()) {

            BigDecimal firstNumber = first.decimalValue();
            BigDecimal secondNumber = second.decimalValue();

            return firstNumber.compareTo(secondNumber);
        }

        // Case-insensitive string comparison.
        if (first.isTextual()) {
            return first.asText().compareToIgnoreCase(second.asText());
        }

        // Boolean comparison: false comes before true.
        if (first.isBoolean()) {
            return Boolean.compare(
                    first.booleanValue(),
                    second.booleanValue()
            );
        }

        throw new IllegalArgumentException(
                "Unsupported JSON value type for sorting"
        );
    }

    public void validateSortableValue(JsonNode value) {

        if (value == null || value.isNull()) {
            return;
        }

        if (!value.isNumber()
                && !value.isTextual()
                && !value.isBoolean()) {

            throw new IllegalArgumentException(
                    "Only numbers, strings and booleans can be sorted"
            );
        }
    }

    private int getTypeOrder(JsonNode value) {

        if (value.isNumber()) {
            return 1;
        }

        if (value.isTextual()) {
            return 2;
        }

        if (value.isBoolean()) {
            return 3;
        }

        throw new IllegalArgumentException(
                "Unsupported JSON value type for sorting"
        );
    }
}
