<div dir="rtl">

# שלב 5.2: ולידציה וטיפול בשגיאות ולידציה

## מה מבוצע
בשלב זה אנו משפרים עוד יותר את האפליקציה על ידי:
1. הוספת ולידציה לשדות המודל באמצעות Jakarta Bean Validation
2. עדכון GlobalExceptionHandler לטיפול בשגיאות ולידציה
3. שימוש באנוטציה @Valid בקונטרולר לאכיפת הולידציה

## הסבר קל
ולידציה היא שכבת הגנה חשובה באפליקציות:
- היא מוודאת שהנתונים המגיעים לאפליקציה עומדים בדרישות לפני שהם מועברים לשכבות הפנימיות
- היא מספקת משוב ברור למשתמשים על טעויות בקלט
- היא מפחיתה את הצורך בבדיקות ידניות בקוד

ב-Spring Boot, אנו משתמשים ב-Jakarta Bean Validation (לשעבר Hibernate Validator) עם אנוטציות כגון:
- `@NotNull` - שדה לא יכול להיות null
- `@NotBlank` - מחרוזת לא יכולה להיות ריקה או null
- `@Size` - מגדיר מגבלות אורך למחרוזות
- `@Min` / `@Max` - מגדיר ערכים מינימליים/מקסימליים למספרים

בשלב זה:
1. הוספנו אנוטציות ולידציה למודל Student
2. הרחבנו את GlobalExceptionHandler לטיפול בחריגות ולידציה
3. הוספנו את אנוטציית @Valid לקונטרולר כדי להפעיל את הולידציה

## קוד מינימלי

</div>

### מודל עם ולידציה - Student.java
```java
package org.example.stage5_2.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
   Lombok, boilerplate code reduction library, is used to generate the getters, setters, equals, hashcode, and toString methods.

   Jakarta Bean Validation - added to validate the fields of the Student class.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "First name is required")
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    private double age;
}
```

<div dir="rtl">

### מחלקת תגובות שגיאה - ErrorResponse.java

</div>

```java
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
```

<div dir="rtl">

### טיפול גלובלי בחריגות עם תמיכה בולידציה - GlobalExceptionHandler.java

</div>

```java
package org.example.stage5_2.exception;

import org.example.stage5_2.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler, this class is used to handle exceptions globally
 * instead of handling them in each controller separately.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * takes care of the exception when a resource is not found, 404 Not Found
     */
    @ExceptionHandler(NotExists.class)
    public ResponseEntity<Object> handleNotExists(NotExists ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Resource Not Found", ex.getMessage()));
    }

    /**
     * takes care of the exception when a resource already exists, 409 Conflict
     * This is more appropriate than 400 Bad Request when trying to create a resource with an ID that already exists
     */
    @ExceptionHandler(AlreadyExists.class)
    public ResponseEntity<Object> handleAlreadyExists(AlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Resource Conflict", ex.getMessage()));
    }

    /**
     * takes care of the exception when there's an ID mismatch, 400 Bad Request
     */
    @ExceptionHandler(StudentIdAndIdMismatch.class)
    public ResponseEntity<Object> handleIdMismatch(StudentIdAndIdMismatch ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ID Mismatch", ex.getMessage()));
    }

    /**
     * takes care of general exceptions, 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error", ex.getMessage()));
    }

    /**
     * Exception handler for @Valid validation errors, such as @NotNull, @Size, etc.
     * BadRequest 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Failed",
                errors.toString()
        );

        // Return HTTP 400 Bad Request with the error response body
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
```

<div dir="rtl">

### קונטרולר עם תמיכה בולידציה - StudentController.java

</div>

```java
package org.example.stage5_2.controller;

import jakarta.validation.Valid;
import org.example.stage5_2.model.Student;
import org.example.stage5_2.service.StudentService;
import org.example.stage5_2.service.StudentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> studentList = studentServiceImpl.getAllStudents();
        return ResponseEntity.ok(studentList); // 200 OK
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        // האנוטציה @Valid תגרום לולידציה של אובייקט הסטודנט לפי ההגדרות במודל
        // אם הולידציה תיכשל, תיזרק חריגת MethodArgumentNotValidException
        Student added = studentServiceImpl.addStudent(student);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();

        return ResponseEntity.created(location).body(added); // 201 Created
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody Student student, @PathVariable Long id) {
        // גם כאן מתבצעת ולידציה של אובייקט הסטודנט
        Student updated = studentServiceImpl.updateStudent(student, id);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentServiceImpl.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה עדיין חסרים:
1. מערכת אחסון נתונים אמיתית (JPA/Hibernate)
2. DTO (Data Transfer Objects) להפרדה בין מודלים פנימיים לחיצוניים
3. בהמשך ...מנגנון אבטחה (Spring Security)


בשלב הבא, נוכל להתמקד בחיבור למסד נתונים והוספת שכבת JPA/Repository.

</div>