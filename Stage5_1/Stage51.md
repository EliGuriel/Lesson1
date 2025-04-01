<div dir="rtl">

# שלב 5.1: טיפול גלובלי בחריגות

## מה מבוצע
בשלב זה אנו מוסיפים טיפול גלובלי בחריגות באמצעות `@ControllerAdvice`, שיפור משמעותי על פני שלב 5 שבו טיפלנו בחריגות בתוך הקונטרולר עצמו. בנוסף, אנו יוצרים מחלקה ייעודית לתגובות שגיאה.

## הסבר קל
טיפול גלובלי בחריגות הוא דפוס מומלץ בפיתוח אפליקציות Spring Boot:
- **@ControllerAdvice** - מאפשר לנו לטפל בחריגות ממקום מרכזי אחד, במקום לחזור על אותו קוד בכל קונטרולר
- **ErrorResponse** - מחלקה ייעודית לתגובות שגיאה מאפשרת פורמט אחיד ועשיר במידע עבור המשתמש
- **HttpStatus** - מוודא שקודי הסטטוס המתאימים מוחזרים עבור כל סוג של חריגה

שיפורים מרכזיים משלב 5:
1. הטיפול בחריגות הועבר מהקונטרולר למחלקה נפרדת עם `@ControllerAdvice`
2. הוספנו מחלקת `ErrorResponse` להחזרת תגובות שגיאה מפורטות יותר
3. הקונטרולר הפך לפשוט יותר ויכול להתמקד בלוגיקה העסקית

## קוד מינימלי

</div>

### מחלקת תגובות שגיאה - ErrorResponse.java
```java
package org.example.stage5_1.model;

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

### מחלקות חריגה

</div>

#### AlreadyExists.java
```java
package org.example.stage5_1.exception;

public class AlreadyExists extends RuntimeException {
    public AlreadyExists(String message) {
        super(message);
    }
}
```

#### NotExists.java
```java
package org.example.stage5_1.exception;

public class NotExists extends RuntimeException {
    public NotExists(String message) {
        super(message);
    }
}
```

#### StudentIdAndIdMismatch.java
```java
package org.example.stage5_1.exception;

public class StudentIdAndIdMismatch extends RuntimeException{
    public StudentIdAndIdMismatch(String message) {
        super(message);
    }
}
```

<div dir="rtl">

### טיפול גלובלי בחריגות - GlobalExceptionHandler.java

</div>

```java
package org.example.stage5_1.exception;

import org.example.stage5_1.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
```

<div dir="rtl">

### קונטרולר פשוט יותר - StudentController.java

</div>

```java
package org.example.stage5_1.controller;

import org.example.stage5_1.model.Student;
import org.example.stage5_1.service.StudentService;
import org.example.stage5_1.service.StudentServiceImpl;
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
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student added = studentServiceImpl.addStudent(student);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();

        return ResponseEntity.created(location).body(added); // 201 Created
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable Long id) {
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

### שירות משודרג - StudentServiceImpl.java (קטע מתוך המחלקה)

</div>

```java
public Student addStudent(Student student) {
    if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
        // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
        throw new AlreadyExists("Student with id " + student.getId() + " already exists");
    }
    students.add(student);
    return student;
}

public Student updateStudent(Student student, Long id) {
    // Check if the ID parameter matches the student's ID
    if (!student.getId().equals(id)) {
        // Exception is thrown and will be handled in the ControllerAdvice GlobalExceptionHandler
        throw new StudentIdAndIdMismatch("Student with id " + id + " mismatch with body id " + student.getId());
    }

    // Use Stream API to find and update the student in one operation
    return students.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .map(existingStudent -> {
                existingStudent.setFirstName(student.getFirstName());
                existingStudent.setLastName(student.getLastName());
                existingStudent.setAge(student.getAge());
                return existingStudent;
            })
            .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה עדיין חסרים:
1. ולידציה של נתונים (Bean Validation) - אנו מטפלים בשגיאות, אך לא מוודאים שהנתונים תקינים מלכתחילה
2. טיפול בשגיאות ולידציה - הוספת מנגנון שיטפל בשגיאות כאשר הנתונים אינם עומדים בתנאי הולידציה
3. תיעוד API באמצעות Swagger או OpenAPI

כדי לעבור לשלב הבא, נצטרך להוסיף מנגנוני ולידציה לשדות המודל ולטפל בשגיאות ולידציה.

</div>