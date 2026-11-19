package com.dataset.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

public class GroupedRecordsResponse {

    private String datasetName;
    private String groupBy;
    private Map<String, List<JsonNode>> groups;

    public GroupedRecordsResponse() {
    }

    public GroupedRecordsResponse(
            String datasetName,
            String groupBy,
            Map<String, List<JsonNode>> groups) {
        this.datasetName = datasetName;
        this.groupBy = groupBy;
        this.groups = groups;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public Map<String, List<JsonNode>> getGroups() {
        return groups;
    }
}