package com.dataset.api.service;

import com.dataset.api.dto.InsertRecordResponse;
import com.dataset.api.entity.DatasetRecord;
import com.dataset.api.repository.DatasetRecordRepository;
import com.dataset.api.validation.DatasetNameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatasetService {

    private final DatasetRecordRepository datasetRecordRepository;
    private final DatasetNameValidator datasetNameValidator;

    public DatasetService(
            DatasetRecordRepository datasetRecordRepository,
            DatasetNameValidator datasetNameValidator) {
        this.datasetRecordRepository = datasetRecordRepository;
        this.datasetNameValidator = datasetNameValidator;
    }

    @Transactional
    public InsertRecordResponse insertRecord(
            String datasetName, JsonNode recordData) {

        // Validate dataset name
        datasetNameValidator.validate(datasetName);

        // Validate JSON record
        if (recordData == null || !recordData.isObject()) {
            throw new IllegalArgumentException(
                    "Record must be a valid JSON object"
            );
        }

        // Create entity
        DatasetRecord datasetRecord =
                new DatasetRecord(datasetName, recordData);

        // Save record
        DatasetRecord savedRecord =
                datasetRecordRepository.save(datasetRecord);

        // Return response
        return new InsertRecordResponse(
                savedRecord.getId(),
                savedRecord.getDatasetName(),
                "Record inserted successfully"
        );
    }
}