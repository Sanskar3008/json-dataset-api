package com.dataset.api.dto;

public class InsertRecordResponse {

    private Long recordId;
    private String datasetName;
    private String message;

    public InsertRecordResponse() {
    }

    public InsertRecordResponse(Long recordId, String datasetName, String message) {
        this.recordId = recordId;
        this.datasetName = datasetName;
        this.message = message;
    }

    public Long getRecordId() {
        return recordId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getMessage() {
        return message;
    }
}