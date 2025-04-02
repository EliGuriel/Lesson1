<div dir="rtl">

# Mission_01_for_Stage5: הנחיות לשדרוג REST API עם Spring Boot

<div dir="rtl"> 

# ResponseEntity בעולם האמיתי - למה צריך אותו ומה מקובל בתעשייה

## למה בכלל צריך ResponseEntity?

באפליקציות אמיתיות, תקשורת HTTP היא הרבה יותר מאשר פשוט להחזיר נתונים. פיתוח מקצועי של שירותי REST דורש:

1. **תקשורת מדויקת עם צרכני ה-API**: הלקוחות צריכים לדעת האם הפעולה הצליחה או נכשלה באמצעות קודי סטטוס תקניים
2. **העברת מידע נוסף**: מעבר לנתונים עצמם, יש צורך בהעברת מטא-דאטה, כותרות, ומידע ניווטי
3. **טיפול עקבי בשגיאות**: ניהול שגיאות בצורה מובנית ועקבית


`ResponseEntity` הוא קלאס מספריית Spring המאפשר שליטה מלאה בתגובת HTTP שמוחזרת מבקר REST. באמצעותו ניתן להגדיר את הגוף, הכותרות וקוד הסטטוס של התגובה.

## יתרונות השימוש ב-ResponseEntity

* שליטה מלאה בקוד הסטטוס של HTTP
* הוספת כותרות HTTP לתגובה
* החזרת גוף התגובה במבנה מותאם
* שיפור האינטגרציה עם ממשקי REST
* עיטוף מאובטח של תגובות

## קודי סטטוס HTTP נפוצים ומתי להשתמש בהם

קודי סטטוס HTTP מתקשרים מידע חשוב ללקוח בנוגע לתוצאת הבקשה:

| קוד | משמעות | מתי להשתמש |
|-----|---------|------------|
| 200 OK | הבקשה הצליחה | GET מוצלח, PUT מוצלח |
| 201 Created | משאב חדש נוצר | POST מוצלח שיצר משאב חדש |
| 204 No Content | הבקשה הצליחה, אין תוכן להחזיר | DELETE מוצלח |
| 400 Bad Request | בקשה לא תקינה | הפרמטרים או הגוף שסופקו שגויים |
| 404 Not Found | המשאב המבוקש לא נמצא | ניסיון לגשת למשאב לא קיים |
| 409 Conflict | התנגשות עם מצב המשאב הנוכחי | ניסיון ליצור משאב שכבר קיים |
| 500 Internal Server Error | שגיאה כללית בשרת | שגיאה לא צפויה בזמן עיבוד |

## מבנה בסיסי של ResponseEntity

`ResponseEntity` מכיל שלושה מרכיבים עיקריים:

1. **קוד סטטוס HTTP**: מספר תלת-ספרתי המציין את תוצאת הבקשה (לדוגמה: 200, 201, 404)
2. **כותרות HTTP**: זוגות מפתח-ערך המספקים מידע נוסף על התגובה
3. **גוף התגובה**: הנתונים המוחזרים ללקוח (אופציונלי, תלוי בקוד הסטטוס)

## מתודות סטטיות נפוצות ב-ResponseEntity

Spring מספק מתודות סטטיות נוחות ליצירת תגובות נפוצות:

* `ResponseEntity.ok()` - יוצר תגובת 200 OK
* `ResponseEntity.created()` - יוצר תגובת 201 Created
* `ResponseEntity.noContent()` - יוצר תגובת 204 No Content
* `ResponseEntity.badRequest()` - יוצר תגובת 400 Bad Request
* `ResponseEntity.notFound()` - יוצר תגובת 404 Not Found
* `ResponseEntity.status(HttpStatus)` - יוצר תגובה עם קוד סטטוס מותאם אישית

## שימוש ב-ResponseEntity עם שגיאות

אחד היתרונות המשמעותיים של `ResponseEntity` הוא היכולת לטפל בשגיאות בצורה אחידה ועקבית. במקום להשתמש במנגנון החריגות הרגיל של Java, ניתן להחזיר תגובות עם קודי סטטוס וגופי שגיאה מותאמים.

כאשר מטפלים בחריגות, אפשר להשתמש בבלוק try-catch כדי לתפוס את החריגה ולהחזיר `ResponseEntity` מתאים עם קוד סטטוס מתאים.

## שימוש ב-URI Builder ליצירת קישורים למשאבים

בעת יצירת משאב חדש, נהוג להחזיר את ה-URI של המשאב החדש בכותרת "Location". Spring מספק את `ServletUriComponentsBuilder` ליצירת URIs דינמיים:

1. `fromCurrentRequest()` - מתחיל מה-URI של הבקשה הנוכחית
2. `path("/{placeholder}")` - מוסיף נתיב יחסי עם מקומות שמורים
3. `buildAndExpand(values)` - מחליף את המקומות השמורים בערכים אמיתיים
4. `toUri()` - ממיר את ה-UriComponents ל-URI

# המשימה

בעת יישום ה-API שלכם, השתמשו ב-`ResponseEntity` כדי לספק תגובות מקצועיות ועקביות שיעזרו לצרכני ה-API שלכם להשתמש בשירות בצורה יעילה ופשוטה.

</div>

## מטרת השדרוג

שדרוג האפליקצייה מתמקד ביישום עקרונות REST מתקדמים, טיפול נכון בשגיאות, ושימוש בסטנדרטים מקצועיים המקובלים בתעשייה.

## מבנה החבילות (Packages)

**חשוב**: מבנה החבילות להלן מוצג כדוגמה בלבד. כל משתלם צריך להשתמש בחבילה האישית שלו. יש לשמור על אותו מבנה תת-חבילות תחת החבילה האישית שלכם.

</div>

```
[חבילה-אישית]
  ├── controller
  │    └── StudentController.java
  ├── exception
  │    ├── AlreadyExists.java
  │    ├── NotExists.java
  │    └── StudentIdAndIdMismatch.java
  ├── model
  │    └── Student.java
  └── service
       ├── StudentService.java
       └── StudentServiceImpl.java
```

<div dir="rtl">

## 1. יצירת מחלקות חריגה מותאמות אישית (Custom Exceptions)

צור את מחלקות החריגה הבאות בחבילה `[חבילה-אישית].exception`:

**1. AlreadyExists.java**

</div>

```java
// Create a custom exception class that extends RuntimeException
// Add a constructor that takes a String message and passes it to the parent constructor
public class AlreadyExists extends RuntimeException {
   // Constructor implementation
}
```

<div dir="rtl">

**2. NotExists.java**

</div>

```java
// Create a custom exception class that extends RuntimeException
// Add a constructor that takes a String message and passes it to the parent constructor
public class NotExists extends RuntimeException {
   // Constructor implementation
}
```

<div dir="rtl">

**3. StudentIdAndIdMismatch.java**

</div>

```java
// Create a custom exception class that extends RuntimeException
// Add a constructor that takes a String message and passes it to the parent constructor
public class StudentIdAndIdMismatch extends RuntimeException {
   // Constructor implementation
}
```

<div dir="rtl">

## 2. שיפור ממשק StudentService

עדכן את ממשק `StudentService` בחבילה `[חבילה-אישית].service` כך שיחזיר אובייקטים אמיתיים ולא מחרוזות תשובה:

</div>

```java
public interface StudentService {
   // Returns a list of all students
   List<Student> getAllStudents();

   // Adds a new student and returns the created student
   // Throws AlreadyExists if a student with the same ID already exists
   Student addStudent(Student student);

   // Updates a student with the specified ID and returns the updated student
   // Takes both the student object and the ID from the path
   // Throws NotExists if the student does not exist
   // Throws StudentIdAndIdMismatch if the ID in the path does not match the ID in the body
   Student updateStudent(Student student, Long id);

   // Deletes a student with the specified ID
   // Throws NotExists if the student does not exist
   void deleteStudent(Long id);
}
```

<div dir="rtl">

## 3. יישום מחלקת StudentServiceImpl

עדכן את `StudentServiceImpl` בחבילה `[חבילה-אישית].service` כך שתשתמש בחריגות מותאמות אישית במקום להחזיר הודעות שגיאה כמחרוזות:

</div>

```java
@Service
public class StudentServiceImpl implements StudentService {

   // Initialize with some example students
   List<Student> students = new ArrayList<>(Arrays.asList(
           new Student(1L, "Alice", "Moskovitz", 21.3),
           new Student(2L, "Bob", "Smith", 22.3),
           new Student(3L, "Charlie", "Brown", 23.3),
           new Student(4L, "David", "Miller", 24.3)
   ));

   // Implementation of getAllStudents
   // Simply returns the list of all students
   public List<Student> getAllStudents() {
      // Return the list of students
   }

   // Implementation of addStudent
   // Check if student with the same ID already exists
   // If exists, throw AlreadyExists exception
   // Otherwise, add the student and return it
   public Student addStudent(Student student) {
      // Implementation code here
   }

   // Implementation of updateStudent
   // Check if the ID in the path matches the ID in the student object
   // If not, throw StudentIdAndIdMismatch exception
   // Check if the student exists
   // If not, throw NotExists exception
   // Otherwise, update the student and return it
   public Student updateStudent(Student student, Long id) {
      // Implementation code here
   }

   // Implementation of deleteStudent
   // Check if the student exists
   // If not, throw NotExists exception
   // Otherwise, delete the student
   public void deleteStudent(Long id) {
      // Implementation code here
   }
}
```

<div dir="rtl">

## 4. שדרוג StudentController

שדרג את ה-Controller בחבילה `[חבילה-אישית].controller` כך שישתמש במוסכמות REST מקובלות ויחזיר תגובות HTTP מתאימות:

</div>

```java
@RestController
@RequestMapping("/students")  // Use plural form for resource names
public class StudentController {

   private final StudentService studentServiceImpl;

   // Constructor injection
   public StudentController(StudentServiceImpl studentServiceImpl) {
      this.studentServiceImpl = studentServiceImpl;
   }

   // GET endpoint to retrieve all students
   // Returns HTTP 200 OK with the list of students
   @GetMapping()
   public ResponseEntity<List<Student>> getAllStudents() {
      // Implementation code here
   }

   // POST endpoint to create a new student
   // Returns HTTP 201 Created with the created student and location header
   // Returns HTTP 400 Bad Request if student already exists
   @PostMapping()
   public ResponseEntity<Object> addStudent(@RequestBody Student student) {
      // Implementation code here
   }

   // PUT endpoint to update a student
   // Takes ID from the path variable
   // Returns HTTP 200 OK with the updated student
   // Returns HTTP 400 Bad Request for ID mismatch or if student doesn't exist
   @PutMapping("/{id}")
   public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable Long id) {
      // Implementation code here
   }

   // DELETE endpoint to remove a student
   // Returns HTTP 204 No Content on successful deletion
   // Returns HTTP 404 Not Found if student doesn't exist
   @DeleteMapping("/{id}")
   public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
      // Implementation code here
   }
}
```

<div dir="rtl">

## 5. מודל Student

מחלקת המודל `Student` בחבילה `[חבילה-אישית].model` נשארת כפי שהיא, ואינה דורשת שינויים:

</div>

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
   Long id;
   String firstName;
   String lastName;
   double age;
}
```

<div dir="rtl">

## סיכום השינויים

1. **שיפור מיפוי ה-REST API**:
   - נתיבים נקיים וסמנטיים יותר
   - שימוש נכון בפעולות HTTP (GET, POST, PUT, DELETE)
   - מיפוי משאבים בצורה מובנית ועקבית

2. **טיפול משופר בשגיאות**:
   - חריגות מותאמות אישית עבור מצבי שגיאה שונים
   - זריקת חריגות במקום החזרת מחרוזות שגיאה
   - תפיסת חריגות והחזרת תגובות שגיאה מובנות

3. **שימוש ב-ResponseEntity**:
   - שליטה מלאה בתגובת HTTP
   - התאמת קודי סטטוס למצבים שונים
   - הוספת כותרות ומידע נוסף לתגובות

4. **שיפור השירותים**:
   - החזרת אובייקטים אמיתיים במקום מחרוזות
   - הוספת בדיקות תקינות ואימות נוספות
   - שיפור החתימות של המתודות

שדרוגים אלה הופכים את ה-API לסטנדרטי יותר, קל יותר לשימוש ותחזוקה, ותואם יותר למוסכמות המקובלות בתעשייה.

</div>