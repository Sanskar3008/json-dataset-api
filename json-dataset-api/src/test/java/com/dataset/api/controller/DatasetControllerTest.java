
package com.dataset.api.controller;

import com.dataset.api.dto.GroupedRecordsResponse;
import com.dataset.api.dto.InsertRecordResponse;
import com.dataset.api.dto.SortedRecordsResponse;
import com.dataset.api.service.DatasetQueryService;
import com.dataset.api.service.DatasetService;
import com.dataset.api.validation.QueryParameterValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DatasetController.class)
class DatasetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DatasetService datasetService;

    @MockBean
    private DatasetQueryService datasetQueryService;

    @MockBean
    private QueryParameterValidator queryParameterValidator;

    @Test
    void shouldInsertRecordSuccessfully() throws Exception {

        InsertRecordResponse response =
                new InsertRecordResponse(
                        1L,
                        "employees",
                        "Record inserted successfully"
                );

        when(datasetService.insertRecord(
                eq("employees"), any(JsonNode.class)))
                .thenReturn(response);

        String requestBody = """
                {
                    "name": "Amit",
                    "age": 25,
                    "department": "IT"
                }
                """;

        mockMvc.perform(post("/api/dataset/employees/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordId").value(1))
                .andExpect(jsonPath("$.datasetName").value("employees"))
                .andExpect(jsonPath("$.message")
                        .value("Record inserted successfully"));
    }

    @Test
    void shouldGetAllRecords() throws Exception {

        List<JsonNode> records = objectMapper.readTree(
                        """
                        [
                            {"name":"Amit","age":25},
                            {"name":"Rahul","age":30}
                        ]
                        """
                ).findParents("name").stream()
                .map(node -> (JsonNode) node)
                .toList();

        when(datasetQueryService.getAllRecords("employees"))
                .thenReturn(records);

        mockMvc.perform(get("/api/dataset/employees/query"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGroupRecordsSuccessfully() throws Exception {

        GroupedRecordsResponse response =
                new GroupedRecordsResponse(
                        "employees",
                        "department",
                        Map.of(
                                "IT",
                                List.of(
                                        objectMapper.readTree(
                                                "{\"name\":\"Amit\"}"),
                                        objectMapper.readTree(
                                                "{\"name\":\"Sneha\"}")
                                ),
                                "HR",
                                List.of(
                                        objectMapper.readTree(
                                                "{\"name\":\"Rahul\"}")
                                )
                        )
                );

        when(datasetQueryService.groupRecords(
                "employees", "department"))
                .thenReturn(response);

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("groupBy", "department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datasetName")
                        .value("employees"))
                .andExpect(jsonPath("$.groups.IT.length()").value(2))
                .andExpect(jsonPath("$.groups.HR.length()").value(1));
    }

    @Test
    void shouldSortRecordsSuccessfully() throws Exception {

        SortedRecordsResponse response =
                new SortedRecordsResponse(
                        "employees",
                        "age",
                        "asc",
                        List.of(
                                objectMapper.readTree(
                                        "{\"name\":\"Sneha\",\"age\":22}"),
                                objectMapper.readTree(
                                        "{\"name\":\"Amit\",\"age\":25}")
                        )
                );

        when(datasetQueryService.sortRecords(
                "employees", "age", "asc"))
                .thenReturn(response);

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("sortBy", "age")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records.length()").value(2))
                .andExpect(jsonPath("$.records[0].age").value(22))
                .andExpect(jsonPath("$.records[1].age").value(25));
    }

    @Test
    void shouldRejectGroupByAndSortByTogether() throws Exception {

        doThrow(new IllegalArgumentException(
                "Group By and Sort By cannot be used together"))
                .when(queryParameterValidator)
                .validateQueryParameters("department", "salary");

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("groupBy", "department")
                        .param("sortBy", "salary"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Group By and Sort By cannot be used together"));
    }

    @Test
    void shouldRejectInvalidJsonRequestBody() throws Exception {

        mockMvc.perform(post("/api/dataset/employees/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json}"))
                .andExpect(status().isBadRequest());
    }
}
