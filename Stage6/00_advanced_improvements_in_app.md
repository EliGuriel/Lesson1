
<div dir="rtl">

# שיפורים מתקדמים באפליקציית Spring Boot


## 1. דירוג תשובות HTTP מתאימים

אחד השיפורים החשובים באפליקציית REST היא החזרת קודי סטטוס HTTP מתאימים. הדבר משפר את חווית המשתמש, מאפשר טיפול טוב יותר בשגיאות בצד הלקוח, ועוקב אחר הסטנדרטים של REST.

### מדוע חשוב להשתמש בקודי סטטוס נכונים?

- **סטנדרטיזציה**: קודי סטטוס HTTP הם חלק מהסטנדרט והלקוחות מצפים להם
- **אבחון שגיאות**: קל יותר לאבחן בעיות כאשר החזרת הסטטוס ספציפית לסוג הבעיה
- **התנהגות אוטומטית של דפדפנים/לקוחות**: דפדפנים ולקוחות אחרים מגיבים באופן שונה לקודי סטטוס שונים

### יישום קודי סטטוס HTTP בבקר

להלן דוגמה כיצד לשפר את בקר הסטודנטים שלנו בעזרת `ResponseEntity`:

</div>

```java
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);  // Status: 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> ResponseEntity.ok(student))  // Status: 200 OK
                .orElse(ResponseEntity.notFound().build());  // Status: 404 Not Found
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Object> addStudent(@RequestBody Student student) {
        try {
            Student added = studentService.addStudent(student);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(added.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(added);  // Status: 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()  // Status: 400 Bad Request
                    .body(new ErrorResponse("Invalid student data", e.getMessage()));
        }
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            student.setId(id);  // וידוא שה-ID תואם את הנתיב
            Student updated = studentService.updateStudent(student);
            return ResponseEntity.ok(updated);  // Status: 200 OK
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  // Status: 404 Not Found
                    .body(new ErrorResponse("Student not found", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()  // Status: 400 Bad Request
                    .body(new ErrorResponse("Invalid data", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();  // Status: 204 No Content
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  // Status: 404 Not Found
                    .body(new ErrorResponse("Student not found", e.getMessage()));
        }
    }
}
```

<div dir="rtl">

### קודי סטטוס HTTP נפוצים ומתי להשתמש בהם

| קוד סטטוס | משמעות | מתי להשתמש |
|-----------|---------|-------------|
| 200 OK | הבקשה הושלמה בהצלחה | בקשות GET, PUT מוצלחות |
| 201 Created | המשאב נוצר בהצלחה | בקשות POST מוצלחות |
| 204 No Content | הבקשה הצליחה, אין תוכן להחזרה | בקשות DELETE מוצלחות |
| 400 Bad Request | בקשה לא תקינה | כשהקלט אינו עומד בולידציה |
| 401 Unauthorized | אימות נדרש | כאשר המשתמש לא מאומת |
| 403 Forbidden | הבקשה אינה מורשית | כאשר למשתמש אין הרשאות |
| 404 Not Found | המשאב לא נמצא | כאשר מבקשים סטודנט שאינו קיים |
| 409 Conflict | התנגשות עם המצב הנוכחי | כאשר מנסים ליצור סטודנט עם ID קיים |
| 500 Internal Server Error | שגיאת שרת | כאשר התרחשה שגיאה בלתי צפויה |

### מחלקות עזר לטיפול בשגיאות

</div>

```java
// מחלקה לייצוג הודעות שגיאה
public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters, setters
}

// מחלקת חריגה לסטודנט שלא נמצא
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
```

<div dir="rtl">

### שינויים בשירות

השירות צריך להיות משודרג גם כן, כך שיחזיר אובייקטים במקום הודעות מחרוזת, ויזרוק חריגות מתאימות:

</div>

```java
@Service
public class StudentService {
    // ... 

    public Optional<Student> getStudentById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Student addStudent(Student student) {
        // בדיקה אם סטודנט כבר קיים
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student with id " + student.getId() + " already exists");
        }
        
        // בדיקות ולידציה
        validateStudent(student);
        
        students.add(student);
        return student;
    }

    public Student updateStudent(Student student) {
        // בדיקה אם סטודנט קיים
        if (students.stream().noneMatch(s -> s.getId().equals(student.getId()))) {
            throw new NotFoundException("Student with id " + student.getId() + " does not exist");
        }
        
        // בדיקות ולידציה
        validateStudent(student);
        
        // מחיקת הקיים והוספת החדש
        students.removeIf(s -> s.getId().equals(student.getId()));
        students.add(student);
        return student;
    }

    public void deleteStudent(Long id) {
        // בדיקה אם סטודנט קיים
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            throw new NotFoundException("Student with id " + id + " does not exist");
        }
        
        students.removeIf(s -> s.getId().equals(id));
    }
    
    private void validateStudent(Student student) {
        // מימוש בסעיף הבא - ולידציה
    }
}
```

<div dir="rtl">

## 2. ולידציה מתקדמת על קלט המשתמש

ולידציה של קלט המשתמש היא חלק קריטי מכל אפליקציה. היא מגינה על יושרת הנתונים, מונעת שגיאות, ומשפרת את האבטחה.

### מדוע ולידציה חשובה?

- **מניעת נתונים לא תקינים**: וידוא שרק נתונים תקינים נשמרים במערכת
- **אבטחה**: הגנה מפני התקפות כמו SQL Injection או XSS
- **חווית משתמש**: מתן משוב מיידי למשתמש על בעיות בקלט

### שימוש ב-Bean Validation (JSR 380)

Spring Boot תומך ב-Bean Validation API, שמאפשר להגדיר אילוצי ולידציה בצורה דקלרטיבית.

ראשית, יש להוסיף את התלות המתאימה ל-`pom.xml`:

</div>

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

<div dir="rtl">

כעת ניתן להוסיף אנוטציות ולידציה למודל:

</div>

```java
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    
    @NotNull(message = "ID cannot be null")
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Min(value = 16, message = "Age should not be less than 16")
    @Max(value = 120, message = "Age should not be greater than 120")
    private double age;
}
```

<div dir="rtl">

בבקר, נוסיף את האנוטציה `@Valid` לפרמטרים שצריכים לעבור ולידציה:

</div>

```java
@PostMapping("/addStudent")
public ResponseEntity<Object> addStudent(@Valid @RequestBody Student student, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Validation failed", errors.toString()));
    }
    
    try {
        Student added = studentService.addStudent(student);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(added);
    } catch (IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Invalid student data", e.getMessage()));
    }
}
```

<div dir="rtl">

### טיפול גלובלי בחריגות ולידציה

במקום לטפל בשגיאות ולידציה בכל שיטה בבקר, ניתן ליצור טיפול גלובלי בעזרת `@ControllerAdvice`:

</div>

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validation failed",
            errors.toString()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Resource not found",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Invalid input",
            ex.getMessage()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Internal server error",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
```

<div dir="rtl">

### ולידציה מותאמת אישית

לעתים, אילוצי הולידציה הסטנדרטיים אינם מספיקים. במקרים אלה, ניתן ליצור ולידציה מותאמת אישית:

1. יצירת אנוטציית ולידציה:

</div>

```java
@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {
    String message() default "Age must be valid for student type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int min() default 16;
    int max() default 120;
}
```

<div dir="rtl">

2. מימוש הולידטור:

</div>

```java
public class AgeRangeValidator implements ConstraintValidator<AgeRange, Double> {
    
    private int min;
    private int max;
    
    @Override
    public void initialize(AgeRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }
    
    @Override
    public boolean isValid(Double age, ConstraintValidatorContext context) {
        if (age == null) {
            return false;
        }
        return age >= min && age <= max;
    }
}
```

<div dir="rtl">

3. שימוש באנוטציה המותאמת אישית:

</div>

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    // שדות אחרים
    
    @AgeRange(min = 16, max = 120, message = "Age must be between 16 and 120")
    private double age;
}
```

<div dir="rtl">

### ולידציה ברמת השירות

מעבר לולידציה ברמת המודל, חשוב לבצע ולידציה נוספת ברמת השירות:

</div>

```java
private void validateStudent(Student student) {
    // בדיקות ולידציה נוספות שלא ניתן לבצע ברמת האנוטציות
    if (student.getFirstName() != null && student.getLastName() != null) {
        if (student.getFirstName().equals(student.getLastName())) {
            throw new IllegalArgumentException("First name and last name cannot be identical");
        }
    }
    
    // בדיקת ייחודיות (מלבד ה-ID)
    boolean duplicateNameExists = students.stream()
        .filter(s -> !s.getId().equals(student.getId())) // מתעלם מההשוואה העצמית בעדכון
        .anyMatch(s -> s.getFirstName().equals(student.getFirstName()) && 
                       s.getLastName().equals(student.getLastName()));
    
    if (duplicateNameExists) {
        throw new IllegalArgumentException("A student with the same first and last name already exists");
    }
}
```

<div dir="rtl">

## שילוב שני הפתרונות - דוגמה מסכמת

על ידי שילוב קודי סטטוס HTTP מתאימים וולידציה מתקדמת, אנו מקבלים API איכותי, עמיד ונוח לשימוש:

- הלקוחות מקבלים קודי סטטוס משמעותיים
- מתבצעת ולידציה מקיפה בכל הרמות
- שגיאות מטופלות בצורה עקבית וברורה
- המשתמשים מקבלים חווית שימוש טובה יותר

### יתרונות השילוב

1. **שילוב מערכות**: שני השיפורים האלה משלימים זה את זה - הולידציה מבטיחה נתונים תקינים, וקודי הסטטוס מעבירים את תוצאות הולידציה בצורה ברורה

2. **חווית מפתח משופרת**: מפתחים המשתמשים ב-API יכולים להבין במהירות את השגיאות ולתקן אותן

3. **תחזוקתיות**: קל יותר לאתר ולתקן בעיות כאשר השגיאות מסווגות בצורה הגיונית

4. **מוכנות לעתיד**: מערכת עם ולידציה טובה וקודי סטטוס מתאימים קלה יותר להרחבה ולשדרוג בעתיד

</div>