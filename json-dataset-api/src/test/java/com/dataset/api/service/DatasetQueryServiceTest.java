package com.dataset.api.service;

import com.dataset.api.dto.GroupedRecordsResponse;
import com.dataset.api.dto.SortedRecordsResponse;
import com.dataset.api.entity.DatasetRecord;
import com.dataset.api.repository.DatasetRecordRepository;
import com.dataset.api.validation.DatasetNameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetQueryServiceTest {

    @Mock
    private DatasetRecordRepository datasetRecordRepository;

    @Mock
    private DatasetNameValidator datasetNameValidator;

    @InjectMocks
    private DatasetQueryService datasetQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<DatasetRecord> records;

    @BeforeEach
    void setUp() throws Exception {

        records = new ArrayList<>();

        records.add(createRecord(
                "Amit", 25, "IT", 50000));

        records.add(createRecord(
                "Rahul", 30, "HR", 65000));

        records.add(createRecord(
                "Sneha", 22, "IT", 45000));

        records.add(createRecord(
                "Priya", 28, "Finance", 70000));
    }

    private DatasetRecord createRecord(
            String name,
            int age,
            String department,
            int salary) throws Exception {

        String json = String.format(
                """
                        {
                            "name": "%s",
                            "age": %d,
                            "department": "%s",
                            "salary": %d
                        }
                        """,
                name, age, department, salary
        );

        JsonNode recordData = objectMapper.readTree(json);

        return new DatasetRecord("employees", recordData);
    }

    @Test
    void shouldFetchAllRecords() {

        when(datasetRecordRepository.findByDatasetName("employees"))
                .thenReturn(records);

        List<JsonNode> result =
                datasetQueryService.getAllRecords("employees");

        assertNotNull(result);
        assertEquals(4, result.size());

        verify(datasetNameValidator).validate("employees");
        verify(datasetRecordRepository)
                .findByDatasetName("employees");
    }

    @Test
    void shouldGroupRecordsByDepartment() {

        when(datasetRecordRepository.findByDatasetName("employees"))
                .thenReturn(records);

        GroupedRecordsResponse response =
                datasetQueryService.groupRecords(
                        "employees", "department");

        assertEquals("employees", response.getDatasetName());
        assertEquals("department", response.getGroupBy());

        assertEquals(3, response.getGroups().size());
        assertEquals(2, response.getGroups().get("IT").size());
        assertEquals(1, response.getGroups().get("HR").size());
        assertEquals(1, response.getGroups().get("Finance").size());

        verify(datasetNameValidator).validate("employees");
    }

    @Test
    void shouldSortRecordsByAgeAscending() {

        when(datasetRecordRepository.findByDatasetName("employees"))
                .thenReturn(records);

        SortedRecordsResponse response =
                datasetQueryService.sortRecords(
                        "employees", "age", "asc");

        List<JsonNode> result = response.getRecords();

        assertEquals(4, result.size());
        assertEquals(22, result.get(0).get("age").asInt());
        assertEquals(25, result.get(1).get("age").asInt());
        assertEquals(28, result.get(2).get("age").asInt());
        assertEquals(30, result.get(3).get("age").asInt());

        verify(datasetNameValidator).validate("employees");
    }

    @Test
    void shouldSortRecordsBySalaryDescending() {

        when(datasetRecordRepository.findByDatasetName("employees"))
                .thenReturn(records);

        SortedRecordsResponse response =
                datasetQueryService.sortRecords(
                        "employees", "salary", "desc");

        List<JsonNode> result = response.getRecords();

        assertEquals(4, result.size());
        assertEquals(70000, result.get(0).get("salary").asInt());
        assertEquals(65000, result.get(1).get("salary").asInt());
        assertEquals(50000, result.get(2).get("salary").asInt());
        assertEquals(45000, result.get(3).get("salary").asInt());

        verify(datasetNameValidator).validate("employees");
    }

    @Test
    void shouldReturnEmptyListWhenDatasetIsEmpty() {

        when(datasetRecordRepository.findByDatasetName("empty"))
                .thenReturn(new ArrayList<>());

        List<JsonNode> result =
                datasetQueryService.getAllRecords("empty");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(datasetNameValidator).validate("empty");
        verify(datasetRecordRepository)
                .findByDatasetName("empty");
    }
}
