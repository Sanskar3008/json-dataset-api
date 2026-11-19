package com.dataset.api.validation;

import org.springframework.stereotype.Component;

@Component
public class QueryParameterValidator {

    public void validateGroupBy(String groupBy) {

        if (groupBy == null || groupBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Group By field cannot be empty"
            );
        }
    }

    public void validateSortBy(String sortBy) {

        if (sortBy == null || sortBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Sort By field cannot be empty"
            );
        }
    }

    public void validateOrder(String order) {

        if (order == null ||
                (!order.equalsIgnoreCase("asc")
                        && !order.equalsIgnoreCase("desc"))) {

            throw new IllegalArgumentException(
                    "Invalid sort order. Allowed values are asc and desc"
            );
        }
    }

    public void validateQueryParameters(String groupBy, String sortBy) {

        if (groupBy != null && sortBy != null) {
            throw new IllegalArgumentException(
                    "Group By and Sort By cannot be used together"
            );
        }

        if (groupBy != null) {
            validateGroupBy(groupBy);
        }

        if (sortBy != null) {
            validateSortBy(sortBy);
        }
    }
}