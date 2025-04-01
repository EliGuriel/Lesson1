<div dir="rtl">

# שלב 3: ארכיטקטורת MVC בסיסית

## מה מבוצע
בשלב זה אנו מיישמים ארכיטקטורת MVC (Model-View-Controller) בסיסית, עם הפרדה לשכבות: מודל, שירות וקונטרולר. אנו מממשים גם אנדפוינט אחד להחזרת רשימת סטודנטים.

## הסבר קל
ארכיטקטורת MVC מאפשרת הפרדה ברורה בין:
- **מודל (Model)**: מייצג את הנתונים ואת הלוגיקה העסקית
- **תצוגה (View)**: מייצג את ממשק המשתמש (בעולם ה-REST, זה בדרך כלל JSON שמוחזר)
- **בקר (Controller)**: מנתב בקשות למתודות המתאימות ומחזיר תגובות

בשלב זה:
- יצרנו מחלקת מודל `Student` עם שדות ומתודות getter/setter
- הוספנו שירות `StudentService` שמכיל רשימה סטטית של סטודנטים
- הוספנו קונטרולר `StudentController` עם אנדפוינט להחזרת כל הסטודנטים
- השתמשנו באנוטציה `@RestController` שמשלבת `@Controller` ו-`@ResponseBody`

## קוד מינימלי

</div>

### מודל - Student.java
```java
package org.example.stage3.model;

public class Student {
    Long id;
    String firstName;
    String lastName;
    double age;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, double age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
```

<div dir="rtl">

### שירות - StudentService.java

</div>

```java
package org.example.stage3.service;

import org.example.stage3.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    List<Student> students = new ArrayList<>(Arrays.asList(
        new Student(1L, "Alice", "Moskovitz", 21.3),
        new Student(2L, "Bob", "Smith", 22.3),
        new Student(3L, "Charlie", "Brown", 23.3),
        new Student(4L, "David", "Miller", 24.3)
    ));

    public List<Student> getAllStudents() {
        return students;
    }
}
```

<div dir="rtl">

### קונטרולר - StudentController.java

</div>

```java
package org.example.stage3.controller;

import org.example.stage3.model.Student;
import org.example.stage3.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentServiceImpl;

    public StudentController(StudentService studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentServiceImpl.getAllStudents();
    }
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה חסרים:
1. פעולות CRUD מלאות (Create, Update, Delete)
2. הפרדה של ממשק שירות מהיישום שלו
3. שימוש בספריות עזר כמו Lombok להפחתת קוד boilerplate

כדי לעבור לשלב הבא, נצטרך להוסיף את הפעולות החסרות ולשפר את הארכיטקטורה.

</div>