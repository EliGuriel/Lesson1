<div dir="rtl">

# שלב 1: אפליקציית Spring Boot בסיסית

## מה מבוצע
בשלב זה אנו יוצרים אפליקציית Spring Boot בסיסית ביותר, שמכילה רק את נקודת הכניסה לאפליקציה.

## הסבר קל
Spring Boot היא מסגרת עבודה המאפשרת פיתוח מהיר של אפליקציות Java. בשלב זה אנו מגדירים את הקלאס הראשי של האפליקציה עם האנוטציה `@SpringBootApplication` שמשלבת מספר אנוטציות חשובות:

</div>

- `@Configuration` - מגדיר את הקלאס כמקור להגדרות
- `@EnableAutoConfiguration` - מאפשר הגדרה אוטומטית של קומפוננטות
- `@ComponentScan` - סורק את החבילה הנוכחית וחבילות המשנה לקומפוננטות

 
<div dir="rtl">

## קוד מינימלי

</div>

```java
package org.example.stage1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Stage1Application {

    public static void main(String[] args) {
        SpringApplication.run(Stage1Application.class, args);
    }
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה אין לנו:
1. קונטרולרים שיטפלו בבקשות HTTP
2. אנדפוינטים שיאפשרו אינטראקציה עם האפליקציה
3. לוגיקה עסקית

כדי לעבור לשלב הבא, נצטרך להוסיף קונטרולר בסיסי עם אנדפוינט אחד לפחות שיחזיר תגובה כלשהי.

</div>