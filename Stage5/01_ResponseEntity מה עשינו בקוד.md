<div dir="rtl">

## מתודות שימושיות ב-ResponseEntity

### מתודות סטטיות

* `ResponseEntity.ok()` - מחזיר סטטוס 200 OK
* `ResponseEntity.created(URI location)` - מחזיר סטטוס 201 Created
* `ResponseEntity.accepted()` - מחזיר סטטוס 202 Accepted
* `ResponseEntity.noContent()` - מחזיר סטטוס 204 No Content
* `ResponseEntity.badRequest()` - מחזיר סטטוס 400 Bad Request
* `ResponseEntity.notFound()` - מחזיר סטטוס 404 Not Found
* `ResponseEntity.status(HttpStatus status)` - מחזיר סטטוס מותאם אישית

### דוגמאות לשימוש במתודות שונות

#### החזרת סטטוס הצלחה עם גוף תגובה

</div>

```java
@GetMapping("/getAllStudents")
public ResponseEntity<List<Student>> getAllStudents() {
    List<Student> studentList = studentService.getAllStudents();
    return ResponseEntity.ok(studentList);
}
```

<div dir="rtl">

#### החזרת סטטוס יצירה (201) עם URI למשאב החדש

</div>

```java
@PostMapping("/addStudent")
public ResponseEntity<Object> addStudent(@RequestBody Student student) {
    try {
        Student added = studentService.addStudent(student);

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
```

<div dir="rtl">

#### החזרת תגובה ללא תוכן

</div>

```java
@DeleteMapping("/deleteStudent/{id}")
public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
    try {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    } catch (NotExists e) {
        return ResponseEntity.notFound().build();
    }
}
```

<div dir="rtl">

## הוספת כותרות HTTP

ניתן להוסיף כותרות HTTP לתגובה:

</div>

```java
@GetMapping("/download/{fileName}")
public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
    Resource resource = fileService.loadFileAsResource(fileName);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
}
```

<div dir="rtl">

## ResponseEntity עם גנריקס

ResponseEntity תומך בגנריקס, כך שניתן להגדיר את הטיפוס המוחזר:

</div>

```java
// מחזיר רשימת סטודנטים
public ResponseEntity<List<Student>> getAllStudents() {
    List<Student> studentList = studentService.getAllStudents();
    return ResponseEntity.ok(studentList);
}

// מחזיר אובייקט סטודנט בודד
public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable Long id) {
    try {
        Student updated = studentService.updateStudent(student, id);
        return ResponseEntity.ok(updated);
    } catch (NotExists | StudentIdAndIdMismatch e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

// מחזיר תגובה ללא נתונים
public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
    try {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    } catch (NotExists e) {
        return ResponseEntity.notFound().build();
    }
}
```

<div dir="rtl">

## טיפול בשגיאות עם ResponseEntity

ResponseEntity מאפשר טיפול אלגנטי בשגיאות:

</div>

```java
@PutMapping("/updateStudent/{id}")
public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable Long id) {
    try {
        Student updated = studentService.updateStudent(student, id);
        return ResponseEntity.ok(updated);
    } catch (NotExists e) {
        // מקרה שבו הסטודנט לא קיים
        return ResponseEntity.notFound().build();
    } catch (StudentIdAndIdMismatch e) {
        // מקרה שבו ה-ID בנתיב אינו תואם את ה-ID באובייקט הסטודנט
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
```

<div dir="rtl">

## ResponseEntity עם ResponseBodyAdvice

ניתן לשלב את ResponseEntity עם `ResponseBodyAdvice` לעיבוד גלובלי של כל התגובות:

</div>

```java
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, 
                                 MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                 ServerHttpRequest request, ServerHttpResponse response) {
        // עטיפת כל תגובות הגוף בפורמט תגובה סטנדרטי
        if (body instanceof StandardResponse) {
            return body;
        }
        return new StandardResponse("success", body, null);
    }
}
```

<div dir="rtl">

## שימוש ב-Builder Pattern

ResponseEntity מספק גם Builder לבנייה מורכבת של תגובות:

</div>

```java
@PostMapping("/addStudent")
public ResponseEntity<Object> addStudent(@RequestBody Student student) {
    try {
        Student added = studentService.addStudent(student);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(added.getId())
            .toUri();

        return ResponseEntity
            .created(location)
            .contentType(MediaType.APPLICATION_JSON)
            .body(added);
    } catch (AlreadyExists e) {
        return ResponseEntity
            .badRequest()
            .body(e.getMessage());
    }
}
```

<div dir="rtl">

## טיפים לשימוש יעיל

1. השתמש במתודות המובנות במקום ליצור אובייקטים ידנית:
   ```
   // מומלץ
   return ResponseEntity.ok(student);
   
   // פחות מומלץ
   return new ResponseEntity<>(student, HttpStatus.OK);
   ```

2. השתמש בבנאים למקרים מורכבים שדורשים כותרות או הגדרות נוספות

3. הקפד על החזרת קודי סטטוס מדויקים ומשמעותיים:
    - 200 OK לבקשות מוצלחות
    - 201 Created ליצירת משאבים חדשים
    - 204 No Content למחיקה מוצלחת
    - 404 Not Found כאשר משאב לא נמצא
    - 400 Bad Request לבקשות שגויות

4. שקול שימוש במחלקת מעטפת אחידה לכל התגובות

5. השתמש בגנריקס כדי לשמור על טיפוסים בטוחים

</div>