<div dir="rtl">

# שלב 5: טיפול בחריגות ושיפור תגובות ה-API

## מה מבוצע
בשלב זה אנו משפרים את האפליקציה על ידי:
1. הוספת טיפול בחריגות מותאם אישית
2. שימוש ב-ResponseEntity להחזרת קודי סטטוס HTTP מתאימים
3. שיפור הקונטרולר כך שישתמש במזהים בנתיב ה-URL בצורה עקבית
4. החזרת אובייקט הסטודנט עצמו במקום מחרוזת טקסט פשוטה

## הסבר קל
שיפורים אלה חשובים מכמה סיבות:
- **טיפול בחריגות מותאם אישית** - מאפשר לנו להגדיר את התנהגות האפליקציה במקרה של שגיאות שונות
- **ResponseEntity** - מאפשר לנו לשלוט בקודי הסטטוס, הכותרות והגוף של התגובה
- **שימוש עקבי במזהים בנתיב** - מתאים לעקרונות RESTful ומאפשר למשתמשים לעבוד באופן אינטואיטיבי עם ה-API

בשלב זה הוספנו:
- מחלקות חריגה ייעודיות: `AlreadyExists`, `NotExists`, `StudentIdAndIdMismatch`
- שימוש ב-ResponseEntity בקונטרולר ששולט בקודי הסטטוס
- החזרת אובייקט השירות במקום מחרוזת טקסט

## קוד מינימלי

</div>

### מחלקות חריגה

#### AlreadyExists.java
```java
package org.example.stage5.exception;

public class AlreadyExists extends RuntimeException {
    public AlreadyExists(String message) {
        super(message);
    }
}
```

#### NotExists.java
```java
package org.example.stage5.exception;

public class NotExists extends RuntimeException {
    public NotExists(String message) {
        super(message);
    }
}
```

#### StudentIdAndIdMismatch.java
```java
package org.example.stage5.exception;

public class StudentIdAndIdMismatch extends RuntimeException{
    public StudentIdAndIdMismatch(String message) {
        super(message);
    }
}
```

<div dir="rtl">

### שירות משופר - StudentService.java

</div>

```java
package org.example.stage5.service;

import org.example.stage5.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    Student addStudent(Student student);

    Student updateStudent(Student student, Long id);

    void deleteStudent(Long id);
}
```

<div dir="rtl">

### יישום השירות המשופר - StudentServiceImpl.java

</div>

```java
package org.example.stage5.service;

import org.example.stage5.exception.AlreadyExists;
import org.example.stage5.exception.NotExists;
import org.example.stage5.exception.StudentIdAndIdMismatch;
import org.example.stage5.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    /**
     * This is a mock database for the sake of this example.
     */
    List<Student> students = new ArrayList<>(Arrays.asList(
        new Student(1L, "Alice", "Moskovitz", 21.3),
        new Student(2L, "Bob", "Smith", 22.3),
        new Student(3L, "Charlie", "Brown", 23.3),
        new Student(4L, "David", "Miller", 24.3)
    ));

    /**
     * This method returns a list of all students.
     *
     * @return List of students
     */
    public List<Student> getAllStudents() {
        return students;
    }

    /**
     * This method adds a new student to the list.
     * @param student the student to be added
     * @return the added student
     */
    public Student addStudent(Student student) {
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new AlreadyExists("Student with id " + student.getId() + " already exists");
        }
        students.add(student);
        return student;
    }

    /**
     * This method updates an existing student.
     * @param student the student to be updated
     * @param id the id of the student to be updated
     * @return the updated student
     */
    public Student updateStudent(Student student, Long id) {
        // Check if the ID parameter matches the student's ID
        if (!student.getId().equals(id)) {
            throw new StudentIdAndIdMismatch("Student with id " + id + " mismatch");
        }

        // Use Stream API to find and update the student in one operation,
        // findFirst return only one element and stops the stream
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

    /**
     * This method deletes a student from the list.
     * @param id the id of the student to be deleted
     */
    public void deleteStudent(Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            throw new NotExists ("Student with id " + id + " does not exist");
        }
        students.removeIf(s -> s.getId().equals(id));
    }
}
```

<div dir="rtl">

### קונטרולר משופר - StudentController.java

</div>

```java
package org.example.stage5.controller;

import org.example.stage5.exception.AlreadyExists;
import org.example.stage5.exception.NotExists;
import org.example.stage5.exception.StudentIdAndIdMismatch;
import org.example.stage5.model.Student;
import org.example.stage5.service.StudentService;
import org.example.stage5.service.StudentServiceImpl;
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
    public ResponseEntity<Object> addStudent(@RequestBody Student student) {
        try {
            Student added = studentServiceImpl.addStudent(student);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(added.getId())
                    .toUri();

            return ResponseEntity.created(location).body(added); // 201 Created
        } catch (AlreadyExists e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable Long id) {
        try {
            Student updated = studentServiceImpl.updateStudent(student, id);
            return ResponseEntity.ok(updated); // 200 OK
        } catch (NotExists | StudentIdAndIdMismatch e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        try {
            studentServiceImpl.deleteStudent(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (NotExists e) {
            return ResponseEntity.notFound().build(); //  notFound()
        }
    }
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה עדיין חסרים:
1. ולידציה של נתונים (Bean Validation)
2. טיפול גלובלי בחריגות באמצעות @ControllerAdvice
3. מחלקה ייעודית לתגובות שגיאה
4. תיעוד טוב יותר של הקוד

כדי לעבור לשלב הבא, נצטרך להוסיף ולידציה לשדות המודל ולשפר את טיפול החריגות הגלובלי.

</div>