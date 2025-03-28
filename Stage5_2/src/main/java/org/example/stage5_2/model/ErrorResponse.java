package org.example.stage5_2.model;

import lombok.Data;

import java.time.LocalDateTime;


/*
 This is used mostly for error handling in the controller.
 If error occurs, this class will be used to return the error message to the client.
 */
@Data
public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}