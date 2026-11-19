package com.dataset.api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatasetNameValidatorTest {

    private DatasetNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DatasetNameValidator();
    }

    @Test
    void shouldAcceptValidDatasetName() {

        assertDoesNotThrow(
                () -> validator.validate("employees")
        );
    }

    @Test
    void shouldAcceptDatasetNameWithNumbers() {

        assertDoesNotThrow(
                () -> validator.validate("employees2026")
        );
    }

    @Test
    void shouldAcceptDatasetNameWithHyphen() {

        assertDoesNotThrow(
                () -> validator.validate("employee-data")
        );
    }

    @Test
    void shouldAcceptDatasetNameWithUnderscore() {

        assertDoesNotThrow(
                () -> validator.validate("employee_data")
        );
    }

    @Test
    void shouldAcceptUppercaseDatasetName() {

        assertDoesNotThrow(
                () -> validator.validate("Employees")
        );
    }

    @Test
    void shouldRejectNullDatasetName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(null)
        );
    }

    @Test
    void shouldRejectEmptyDatasetName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate("")
        );
    }

    @Test
    void shouldRejectDatasetNameStartingWithNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate("123employees")
        );
    }

    @Test
    void shouldRejectDatasetNameWithSpecialCharacters() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate("employee@data")
        );
    }

    @Test
    void shouldRejectDatasetNameExceeding100Characters() {

        String datasetName = "a".repeat(101);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(datasetName)
        );
    }

    @Test
    void shouldAcceptDatasetNameWithExactly100Characters() {

        String datasetName = "a".repeat(100);

        assertDoesNotThrow(
                () -> validator.validate(datasetName)
        );
    }
}
