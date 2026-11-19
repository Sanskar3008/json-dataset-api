package com.dataset.api.service;

import com.dataset.api.dto.InsertRecordResponse;
import com.dataset.api.entity.DatasetRecord;
import com.dataset.api.repository.DatasetRecordRepository;
import com.dataset.api.validation.DatasetNameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetServiceTest {

    @Mock
    private DatasetRecordRepository datasetRecordRepository;

    @Mock
    private DatasetNameValidator datasetNameValidator;

    @InjectMocks
    private DatasetService datasetService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldInsertRecordSuccessfully() throws Exception {

        JsonNode recordData = objectMapper.readTree(
                """
                        {
                            "name": "Amit",
                            "age": 25,
                            "department": "IT"
                        }
                        """
        );

        when(datasetRecordRepository.save(any(DatasetRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InsertRecordResponse response =
                datasetService.insertRecord("employees", recordData);

        assertNotNull(response);
        assertEquals("employees", response.getDatasetName());
        assertEquals(
                "Record inserted successfully",
                response.getMessage()
        );

        verify(datasetNameValidator).validate("employees");
        verify(datasetRecordRepository).save(any(DatasetRecord.class));
    }

    @Test
    void shouldRejectInvalidJsonRecord() throws Exception {

        JsonNode recordData = objectMapper.readTree(
                """
                        ["Amit", "Rahul"]
                        """
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> datasetService.insertRecord("employees", recordData)
        );

        verify(datasetNameValidator).validate("employees");
        verify(datasetRecordRepository, never())
                .save(any(DatasetRecord.class));
    }

    @Test
    void shouldRejectNullRecord() {

        assertThrows(
                IllegalArgumentException.class,
                () -> datasetService.insertRecord("employees", null)
        );

        verify(datasetRecordRepository, never())
                .save(any(DatasetRecord.class));
    }
}
