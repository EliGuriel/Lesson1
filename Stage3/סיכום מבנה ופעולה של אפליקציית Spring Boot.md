<div dir="rtl">

# מבנה ופעולה של אפליקציית Spring Boot

## מבנה הפרויקט

אפליקציית Spring Boot שהוצגה מאורגנת בארכיטקטורת שכבות ברורה, שמופרדת לתתי-חבילות:

- **model**: מכיל את מחלקת `Student` המייצגת את מודל הנתונים
- **service**: מכיל את `StudentService` המטפל בלוגיקה העסקית
- **controller**: מכיל את `StudentController` המטפל בבקשות HTTP

ארגון זה מיישם את עקרון ההפרדה בין רכיבים (Separation of Concerns), שמקל על תחזוקה, הרחבה ובדיקה של היישום.

## בדיקת האפליקציה

קיימות מספר דרכים לבדוק את האפליקציה:

### 1. שימוש ב-HTTP Client של IntelliJ Ultimate

IntelliJ Ultimate מכיל כלי מובנה לביצוע בקשות HTTP:

1. יצירת קובץ `.http` או `.rest` בפרויקט
2. כתיבת בקשה בסיסית:
```
GET http://localhost:8080/student/getAllStudents
Accept: application/json
```
3. לחיצה על האייקון "Run" ליד הבקשה

יתרונות:
- משולב בסביבת הפיתוח
- תמיכה בתחביר ב-IDE
- שמירת היסטוריית תגובות
- אפשרות לכתוב סקריפטים של בקשות

### 2. שימוש בדפדפן

לבקשות GET פשוטות:
1. פתיחת דפדפן
2. ניווט ל-`http://localhost:8080/student/getAllStudents`

יתרונות:
- פשטות
- גישה מהירה לבקשות GET
- תצוגה ידידותית של JSON (בדפדפנים מודרניים)

מגבלות:
- מוגבל בעיקר לבקשות GET
- פחות נוח עבור פרמטרים מורכבים

### 3. שימוש ב-Postman

Postman הוא כלי עוצמתי לבדיקת API:

1. התקנת Postman
2. יצירת בקשה חדשה:
    - בחירת סוג הבקשה (GET)
    - הזנת URL: `http://localhost:8080/student/getAllStudents`
    - לחיצה על "Send"

יתרונות:
- תמיכה בכל סוגי הבקשות (GET, POST, PUT, DELETE וכו')
- ממשק גרפי ידידותי
- אפשרות לשמור בקשות באוספים
- ניתן ליצור סביבות עבודה שונות (פיתוח, בדיקות, ייצור)

## הגדרת לוגים מפורטים

הוספת הגדרה ל-`application.properties`:

```properties
logging.level.org.springframework.web=DEBUG
```

יתרונות הלוגים המפורטים:
- הצגת מידע מפורט על בקשות HTTP הנכנסות
- הצגת מסלול הניתוב של הבקשה
- מידע על הפרמטרים שהתקבלו
- מעקב אחרי תהליך המרת התגובה
- שימושי לפתרון בעיות וניפוי שגיאות

## הזרקת תלויות (Dependency Injection)

האפליקציה משתמשת בהזרקת תלויות, אחד מעקרונות היסוד של Spring:

```java
private final StudentService studentService;

public StudentController(StudentService studentService) {
    this.studentService = studentService;
}
```

### יתרונות הזרקת תלויות:

1. **צימוד חלש (Loose Coupling)**:
    - הרכיבים אינם יוצרים ישירות את התלויות שלהם
    - רכיבים תלויים בהפשטות (interfaces) ולא במימושים ספציפיים

2. **יכולת בדיקה (Testability)**:
    - קל יותר ליצור בדיקות יחידה
    - אפשר להחליף תלויות אמיתיות ב-mocks או stubs

3. **מודולריות**:
    - קל להחליף מימושים של שירותים
    - התאמה לתרחישים שונים ללא שינוי בקוד הלקוח

4. **ניהול מחזור חיים**:
    - Spring מנהל את מחזור החיים של ה-beans
    - יכולת לנהל היקף (scope) - singleton, prototype, request, session

5. **פחות קוד boilerplate**:
    - אין צורך בקוד אתחול מורכב
    - Spring מטפל ביצירת האובייקטים וקישור התלויות

### אופן היישום ב-Spring Boot:

- **אנוטציית `@Service`**: מסמנת את `StudentService` כ-bean של שירות
- **אנוטציית `@RestController`**: מסמנת את `StudentController` כ-bean של בקר REST
- **הזרקה בבנאי**: Spring מזריק אוטומטית את ה-bean המתאים בזמן יצירת הבקר

Spring מסתמך על הקונבנציה שמימוש אחד יהיה זמין לכל תלות, או שתהיה אנוטציה המציינת איזה מימוש יש להזריק כאשר יש מספר אפשרויות.

</div>