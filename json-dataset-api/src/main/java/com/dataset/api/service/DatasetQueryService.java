package com.dataset.api.service;

import com.dataset.api.dto.GroupedRecordsResponse;
import com.dataset.api.dto.SortedRecordsResponse;
import com.dataset.api.entity.DatasetRecord;
import com.dataset.api.repository.DatasetRecordRepository;
import com.dataset.api.util.JsonValueComparator;
import com.dataset.api.validation.DatasetNameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatasetQueryService {

    private final DatasetRecordRepository datasetRecordRepository;
    private final DatasetNameValidator datasetNameValidator;

    public DatasetQueryService(
            DatasetRecordRepository datasetRecordRepository,
            DatasetNameValidator datasetNameValidator) {
        this.datasetRecordRepository = datasetRecordRepository;
        this.datasetNameValidator = datasetNameValidator;
    }

    @Transactional(readOnly = true)
    public List<JsonNode> getAllRecords(String datasetName) {

        datasetNameValidator.validate(datasetName);

        List<DatasetRecord> records =
                datasetRecordRepository.findByDatasetName(datasetName);

        List<JsonNode> result = new ArrayList<>();

        for (DatasetRecord record : records) {
            result.add(record.getRecordData());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public GroupedRecordsResponse groupRecords(
            String datasetName, String groupBy) {

        datasetNameValidator.validate(datasetName);

        List<DatasetRecord> records =
                datasetRecordRepository.findByDatasetName(datasetName);

        Map<String, List<JsonNode>> groups = new LinkedHashMap<>();

        for (DatasetRecord record : records) {

            JsonNode fieldValue = record.getRecordData().get(groupBy);

            String key;

            if (fieldValue == null || fieldValue.isNull()) {
                key = "null";
            } else if (fieldValue.isTextual()) {
                key = fieldValue.asText();
            } else if (fieldValue.isValueNode()) {
                key = fieldValue.toString();
            } else {
                throw new IllegalArgumentException(
                        "Cannot group by an array or object field: " + groupBy
                );
            }

            groups.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(record.getRecordData());
        }

        return new GroupedRecordsResponse(
                datasetName,
                groupBy,
                groups
        );
    }

    @Transactional(readOnly = true)
    public SortedRecordsResponse sortRecords(
            String datasetName,
            String sortBy,
            String order) {

        datasetNameValidator.validate(datasetName);

        List<DatasetRecord> records =
                datasetRecordRepository.findByDatasetName(datasetName);

        JsonValueComparator valueComparator = new JsonValueComparator();

        Comparator<DatasetRecord> comparator = (first, second) -> {

            JsonNode firstValue = first.getRecordData().get(sortBy);
            JsonNode secondValue = second.getRecordData().get(sortBy);

            int comparison =
                    valueComparator.compare(firstValue, secondValue);

            boolean firstIsNull =
                    firstValue == null || firstValue.isNull();

            boolean secondIsNull =
                    secondValue == null || secondValue.isNull();

            // Reverse only non-null comparisons for descending order.
            // Null and missing values remain last.
            if ("desc".equalsIgnoreCase(order)
                    && !firstIsNull && !secondIsNull) {
                comparison = -comparison;
            }

            // Keep sorting deterministic when values are equal.
            if (comparison == 0) {
                return first.getId().compareTo(second.getId());
            }

            return comparison;
        };

        records.sort(comparator);

        List<JsonNode> sortedRecords = new ArrayList<>();

        for (DatasetRecord record : records) {
            sortedRecords.add(record.getRecordData());
        }

        return new SortedRecordsResponse(
                datasetName,
                sortBy,
                order,
                sortedRecords
        );
    }
}
