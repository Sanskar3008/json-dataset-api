package com.dataset.api.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class SortedRecordsResponse {

    private String datasetName;
    private String sortBy;
    private String order;
    private List<JsonNode> records;

    public SortedRecordsResponse() {
    }

    public SortedRecordsResponse(
            String datasetName,
            String sortBy,
            String order,
            List<JsonNode> records) {
        this.datasetName = datasetName;
        this.sortBy = sortBy;
        this.order = order;
        this.records = records;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getOrder() {
        return order;
    }

    public List<JsonNode> getRecords() {
        return records;
    }
}