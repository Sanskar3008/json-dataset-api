package com.dataset.api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParameterValidatorTest {

    private QueryParameterValidator validator;

    @BeforeEach
    void setUp() {
        validator = new QueryParameterValidator();
    }

    @Test
    void shouldAcceptValidGroupBy() {

        assertDoesNotThrow(
                () -> validator.validateGroupBy("department")
        );
    }

    @Test
    void shouldRejectNullGroupBy() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateGroupBy(null)
        );
    }

    @Test
    void shouldRejectBlankGroupBy() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateGroupBy(" ")
        );
    }

    @Test
    void shouldAcceptValidSortBy() {

        assertDoesNotThrow(
                () -> validator.validateSortBy("salary")
        );
    }

    @Test
    void shouldRejectBlankSortBy() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSortBy("")
        );
    }

    @Test
    void shouldAcceptAscendingOrder() {

        assertDoesNotThrow(
                () -> validator.validateOrder("asc")
        );
    }

    @Test
    void shouldAcceptDescendingOrder() {

        assertDoesNotThrow(
                () -> validator.validateOrder("desc")
        );
    }

    @Test
    void shouldAcceptOrderIgnoringCase() {

        assertDoesNotThrow(
                () -> validator.validateOrder("DESC")
        );
    }

    @Test
    void shouldRejectInvalidOrder() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateOrder("random")
        );
    }

    @Test
    void shouldRejectGroupByAndSortByTogether() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateQueryParameters(
                        "department", "salary")
        );
    }

    @Test
    void shouldAcceptGroupByWithoutSortBy() {

        assertDoesNotThrow(
                () -> validator.validateQueryParameters(
                        "department", null)
        );
    }

    @Test
    void shouldAcceptSortByWithoutGroupBy() {

        assertDoesNotThrow(
                () -> validator.validateQueryParameters(
                        null, "salary")
        );
    }
}
