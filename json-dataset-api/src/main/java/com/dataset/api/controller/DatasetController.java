
package com.dataset.api.controller;

import com.dataset.api.dto.GroupedRecordsResponse;
import com.dataset.api.dto.InsertRecordResponse;
import com.dataset.api.dto.SortedRecordsResponse;
import com.dataset.api.service.DatasetQueryService;
import com.dataset.api.service.DatasetService;
import com.dataset.api.validation.QueryParameterValidator;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dataset")
@Tag(
        name = "Dataset API",
        description = "APIs to insert, retrieve, group and sort JSON dataset records"
)
public class DatasetController {

    private final DatasetService datasetService;
    private final DatasetQueryService datasetQueryService;
    private final QueryParameterValidator queryParameterValidator;

    public DatasetController(
            DatasetService datasetService,
            DatasetQueryService datasetQueryService,
            QueryParameterValidator queryParameterValidator) {

        this.datasetService = datasetService;
        this.datasetQueryService = datasetQueryService;
        this.queryParameterValidator = queryParameterValidator;
    }

    @PostMapping("/{datasetName}/record")
    @Operation(
            summary = "Insert a JSON record",
            description = "Inserts a JSON object into the specified dataset."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Record inserted successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid dataset name or JSON request body"),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error")
    })
    public ResponseEntity<InsertRecordResponse> insertRecord(
            @Parameter(description = "Name of the dataset", example = "employees")
            @PathVariable String datasetName,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON object to insert",
                    required = true
            )
            @RequestBody JsonNode recordData) {

        InsertRecordResponse response =
                datasetService.insertRecord(datasetName, recordData);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{datasetName}/query")
    @Operation(
            summary = "Query dataset records",
            description = """
                    Retrieves all records, groups records by a JSON field,
                    or sorts records by a JSON field.

                    Use either groupBy or sortBy, not both.
                    If neither is supplied, all records are returned.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Records retrieved successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid query parameters or dataset name"),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error")
    })
    public ResponseEntity<?> queryRecords(

            @Parameter(description = "Name of the dataset", example = "employees")
            @PathVariable String datasetName,

            @Parameter(description = "JSON field used for grouping", example = "department")
            @RequestParam(required = false) String groupBy,

            @Parameter(description = "JSON field used for sorting", example = "age")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Sort direction: asc or desc", example = "asc")
            @RequestParam(defaultValue = "asc") String order) {

        queryParameterValidator.validateQueryParameters(
                groupBy, sortBy);

        if (groupBy != null) {

            GroupedRecordsResponse response =
                    datasetQueryService.groupRecords(
                            datasetName, groupBy);

            return ResponseEntity.ok(response);
        }

        if (sortBy != null) {

            queryParameterValidator.validateOrder(order);

            SortedRecordsResponse response =
                    datasetQueryService.sortRecords(
                            datasetName, sortBy, order);

            return ResponseEntity.ok(response);
        }

        List<JsonNode> records =
                datasetQueryService.getAllRecords(datasetName);

        return ResponseEntity.ok(records);
    }
}
