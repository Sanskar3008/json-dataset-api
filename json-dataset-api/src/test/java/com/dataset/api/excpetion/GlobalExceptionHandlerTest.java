package com.dataset.api.excpetion;




import com.dataset.api.exception.ErrorResponse;
import com.dataset.api.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/dataset/employees/query");
    }

    @Test
    void shouldReturn400ForIllegalArgumentException() {

        IllegalArgumentException exception =
                new IllegalArgumentException("Invalid dataset name");

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleIllegalArgumentException(
                        exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid dataset name",
                response.getBody().getMessage());
        assertEquals("/api/dataset/employees/query",
                response.getBody().getPath());
    }

    @Test
    void shouldReturn400ForInvalidJson() {

        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException("Invalid JSON");

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleInvalidJson(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid or missing JSON request body",
                response.getBody().getMessage());
    }



    @Test
    void shouldReturn500ForUnexpectedException() {

        Exception exception =
                new RuntimeException("Database connection failed");

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleGenericException(
                        exception, request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred",
                response.getBody().getMessage());
    }
}
