package com.dataset.api.validation;

import org.springframework.stereotype.Component;

@Component
public class DatasetNameValidator {

    private static final String DATASET_NAME_PATTERN =
            "^[a-zA-Z][a-zA-Z0-9_-]{0,99}$";

    public void validate(String datasetName) {

        if (datasetName == null || !datasetName.matches(DATASET_NAME_PATTERN)) {
            throw new IllegalArgumentException(
                    "Invalid dataset name. It must start with a letter and contain only letters, numbers, hyphens or underscores. Maximum length is 100."
            );
        }
    }
}