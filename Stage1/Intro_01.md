<div dir="rtl">

# מדריך התחלתי ל-Spring Boot

## מבוא לפיתוח עם Spring Boot

### שלב 1: יצירת פרויקט בסיסי
- יצירת פרויקט באמצעות Spring Initializer (https://start.spring.io/) עם תלות Web בלבד
- פתיחת הפרויקט ב-IntelliJ Ultimate
- סקירת מבנה הפרויקט הבסיסי

### שלב 2: הבנת מחלקת המוצא (Main Class)

</div>

```java
package org.example.firstmeeting1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstMeeting1Application {

    public static void main(String[] args) {
        SpringApplication.run(FirstMeeting1Application.class, args);
    }
}
```

<div dir="rtl">

### הסבר על מחלקת המוצא:
- האנוטציה `SpringBootApplication@` מסמנת את המחלקה כאפליקציית Spring Boot ומשלבת כמה אנוטציות שימושיות יחד:

</div>



<div dir="ltr">

    - `@Configuration` - מסמנת את המחלקה כמקור להגדרות תצורה
    - `@EnableAutoConfiguration` - מאפשרת הגדרה אוטומטית של רכיבים בהתאם לספריות במסלול הקלאסים
    - `@ComponentScan` - מגדירה היכן לחפש רכיבי Spring (בדרך כלל בחבילה הנוכחית ותת-חבילות)

</div>


<div dir="rtl">

- השורה `SpringApplication.run(FirstMeeting1Application.class, args)` מתחילה את האפליקציה:
    - יוצרת Context אפליקציה של Spring
    - מפעילה סריקת רכיבים
    - רושמת Bean-ים
    - מעלה שרת Tomcat מוטמע
    - מגדירה את הסביבה

### שלב 3: הבנת פלט ההרצה

</div>

<div dir="ltr">

```
2025-03-12T22:49:12.974+02:00  INFO 32688 --- [FirstMeeting1] [           main] o.e.f.FirstMeeting1Application : Starting FirstMeeting1Application using Java 23.0.2 with PID 32688
```

</div>

<div dir="rtl">

- מראה זמן וידיעה (INFO)
- מציג את מזהה התהליך (PID): 32688
- מציין שהאפליקציה מתחילה לרוץ על Java 23.0.2
</div>


<div dir="ltr">

```
2025-03-12T22:49:12.975+02:00  INFO 32688 --- [FirstMeeting1] [           main] o.e.f.FirstMeeting1Application : No active profile set, falling back to 1 default profile: "default"
```

</div>

<div dir="rtl">
- מציין שאין פרופיל פעיל מוגדר, משתמש בפרופיל ברירת מחדל
</div>


<div dir="ltr">

```
2025-03-12T22:49:13.338+02:00  INFO 32688 --- [FirstMeeting1] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat initialized with port 8080 (http)
```

</div>

<div dir="rtl">
- שרת Tomcat מוטמע אותחל על פורט 8080
</div>


<div dir="ltr">

```
2025-03-12T22:49:13.364+02:00  INFO 32688 --- [FirstMeeting1] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 363 ms
```

</div>

<div dir="rtl">
- הקשר האפליקציה של האינטרנט אותחל בהצלחה ב-363 מילישניות
</div>


<div dir="ltr">

```
2025-03-12T22:49:13.518+02:00  INFO 32688 --- [FirstMeeting1] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port 8080 (http) with context path '/'
```

</div>

<div dir="rtl">
- שרת Tomcat החל לפעול על פורט 8080 עם נתיב הקשר '/'
</div>


<div dir="ltr">

```
2025-03-12T22:49:13.521+02:00  INFO 32688 --- [FirstMeeting1] [           main] o.e.f.FirstMeeting1Application : Started FirstMeeting1Application in 0.759 seconds (process running for 1.098)
```

</div>

<div dir="rtl">
- האפליקציה הופעלה במלואה תוך 0.759 שניות

### שלב 4: בדיקת התהליך הפועל

#### באמצעות CMD (Windows)
הפקודה `netstat -aon` טובה לבדיקת פורטים שמאזינים, אך ישנן פקודות טובות יותר לבדיקת תהליכים ספציפיים:

</div>

```cmd
# רק לראות את התהליך לפי PID:
tasklist /FI "PID eq 32688"

# לראות את כל התהליכים של Java:
tasklist /FI "IMAGENAME eq java.exe"

# לראות מי מאזין על פורט 8080 ספציפית:
netstat -ano | findstr :8080

# לקבל מידע מפורט יותר:
wmic process where processid=32688 get commandline,name,processid,executablepath
```

<div dir="rtl">

### שלב 5: בדיקת האפליקציה בדפדפן

1. פתיחת דפדפן ב-`http://localhost:8080`
2. הסבר על דף ה-Whitelabel Error Page:
    - זהו דף שגיאה המוצג כי אין לנו עדיין בקר (Controller) שמטפל בבקשות
    - מראה שהשרת פועל אך אין תוכן שהוגדר לנתיב השורש


## נקודות חשובות להדגשה בשיעור:

1. **מבנה פרויקט Spring Boot**:
    - קובץ `pom.xml` (עבור Maven) או `build.gradle` (עבור Gradle) המגדיר תלויות
    - חבילות מאורגנות בצורה היררכית
    - קובץ `application.properties` להגדרות תצורה

2. **מחזור החיים של אפליקציית Spring Boot**:
    - אתחול הקשר האפליקציה
    - טעינת Bean-ים וקונפיגורציות
    - הפעלת שרת מוטמע
    - ניהול בקשות

3. **יתרונות Spring Boot**:
    - הגדרה אוטומטית של רכיבים
    - שרת מוטמע מובנה
    - ניהול תלויות פשוט
    - קלות הפיתוח והפריסה

</div>