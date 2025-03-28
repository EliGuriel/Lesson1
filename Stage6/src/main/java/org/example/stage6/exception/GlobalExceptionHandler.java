package org.example.stage6.exception;

import org.example.stage6.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
/*
@ControllerAdvice is a specialization of @Component, which allows handling exceptions
across the whole application in one global handling part.
 */
/*
 * GlobalExceptionHandler - A centralized exception handling component
 *
 * This class captures exceptions thrown across the application and provides a consistent
 * error response structure to clients. It leverages Spring's @ExceptionHandler mechanism
 * to intercept specific exception types and transform them into appropriate HTTP responses.
 *
 */

/*
 * The Benefits of Using GlobalExceptionHandler and Where Each Exception Appears in the Code:
 *
 * 1. MethodArgumentNotValidException:
 *    Source: Thrown when object-level validations fail
 *    Appearances in code:
 *    - In Student.java: All annotations @NotBlank, @Size, @Min, @Max, etc.
 *    - In StudentController.java: Wherever @Valid is attached to a parameter, for example:
 *      * @Valid @RequestBody Student in addStudent and updateStudent methods
 *      * @Valid @PathVariable Long id in getStudentById and deleteStudent methods
 *
 * 2. NotFoundException:
 *    Source: Manually thrown when a requested resource is not found
 *    Appearances in code:
 *    - In StudentService.java:
 *      * In updateStudent method: "if (students.stream().noneMatch(s -> s.getId().equals(student.getId())))"
 *      * In deleteStudent method: "if (students.stream().noneMatch(s -> s.getId().equals(id)))"
 *    - In the improved StudentController.java:
 *      * In getStudentById method: "studentService.getStudentById(id).orElseThrow(() -> new NotFoundException(...))"
 *
 * 3. IllegalArgumentException:
 *    Source: Manually thrown when there's a problem with the data not related to simple validation
 *    Appearances in code:
 *    - In StudentService.java:
 *      * In addStudent method: "if (students.stream().anyMatch(s -> s.getId().equals(student.getId())))"
 *      * In validateStudent method: "if (student.getFirstName().equals(student.getLastName()))"
 *      * In validateStudent method: The check for duplicate names
 *    - In the improved StudentController.java:
 *      * In updateStudent method: ID mismatch check between path and body
 *
 * 4. General Exception:
 *    Source: Any other unexpected exception in the application
 *    This serves as a safety net for all unhandled exceptions, providing clean error responses
 *    instead of stack traces or error pages to the client.
 *
 * Key Advantages of GlobalExceptionHandler:
 * - Centralized exception handling in one place
 * - Consistent error response format across the application
 * - Clean separation between business logic and error handling
 * - Improved code readability in controllers (focus on happy path)
 * - Simplified maintenance and future modifications to error handling
 */
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions that occur during request parameter binding
     * and validation process.
     *
     * This method specifically processes MethodArgumentNotValidException which is thrown
     * when @Valid or @Validated annotated parameters fail validation. Unlike other exception
     * handlers, this creates a Map to collect multiple validation errors that may occur
     * simultaneously across different fields in the request payload.
     *
     * The Map structure allows the client to receive detailed information about:
     * - Which specific fields failed validation
     * - What validation rules were violated for each field
     *
     * For example, in a user registration form with invalid email and password:
     * {"email": "must be a valid email address", "password": "must be at least 8 characters"}
     *
     * @param ex The validation exception containing binding result with all field errors
     * @return ResponseEntity with 400 Bad Request status and structured error details
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Create a map to store field-specific validation errors
        // Key: the field name (e.g., "email", "password")
        // Value: the validation error message (e.g., "must not be empty")
        Map<String, String> errors = new HashMap<>();

        // Iterate through all field errors in the binding result
        // For each error, extract the field name and corresponding error message,
        // Then populate the error map with these field-message pairs
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Create a standardized error response object with:
        // - A general error message indicating validation failure
        // - The detailed map of all validation errors converted to string
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                errors.toString()
        );

        // Return HTTP 400 Bad Request with the error response body
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles resource not found exceptions.
     *
     * This method processes custom NotFoundException, which is typically thrown when
     * a requested resource (user, product, etc.) cannot be found in the system.
     * Unlike the validation handler, this only deals with a single error message
     * because a "not found" condition is a single failure state.
     *
     * @param ex The not found exception with the specific resource identification
     * @return ResponseEntity with 404 Didn't Find status and error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        // Create a standardized error response with:
        // - A general error message indicating resource not found
        // - The specific message from the exception (e.g., "User with ID 123 not found")
        ErrorResponse errorResponse = new ErrorResponse(
                "Resource not found",
                ex.getMessage()
        );

        // Return HTTP 404 Not Found with the error response body
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles illegal argument exceptions.
     *
     * This method processes IllegalArgumentException, which is typically thrown when
     * the input parameters violate business logic constraints beyond simple validation.
     * For example, when a date range has end date before start date, or when a transfer
     * amount exceeds available balance.
     *
     * @param ex The illegal argument exception with details about the constraint violation
     * @return ResponseEntity with 400 Bad Request status and error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Create a standardized error response with:
        // - A general error message indicating invalid input
        // - The specific business rule violation message
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid input",
                ex.getMessage()
        );

        // Return HTTP 400 Bad Request with the error response body
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Fallback handler for all other unhandled exceptions.
     *
     * This method acts as a safety net to catch any exceptions not explicitly handled
     * by other exception handlers. It ensures that clients receive a proper error response
     * rather than a default error page or stack trace when unexpected errors occur.
     *
     * In a production environment, you might want to:
     * - Log the exception details for troubleshooting
     * - Hide technical details from the response
     * - Generate a unique error reference ID for support purposes
     *
     * @param ex Any exception not caught by more specific handlers
     * @return ResponseEntity with 500 Internal Server Error status and error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Create a standardized error response with:
        // - A general error message indicating server error
        // - The exception message (in production, you might want to hide this)
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                ex.getMessage()
        );

        // Return HTTP 500 Internal Server Error with the error response body
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


