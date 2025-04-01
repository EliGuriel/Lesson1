
<div dir="rtl">

# שלב 2: הוספת קונטרולר בסיסי

## מה מבוצע
בשלב זה אנו מוסיפים קונטרולר בסיסי עם אנדפוינט פשוט שמחזיר הודעת טקסט.

## הסבר קל
קונטרולרים ב-Spring Boot אחראים לטיפול בבקשות HTTP ולהחזרת תגובות מתאימות. בדוגמה זו:
- האנוטציה `@Controller` מסמנת את הקלאס כקונטרולר
- `@RequestMapping("/welcome")` מגדיר את נתיב הבסיס של הקונטרולר
- `@ResponseBody` מציינת שהערך שמוחזר מהמתודה יוחזר כתוכן הגוף של התגובה
- המתודה `greet()` מטפלת בבקשות לנתיב "/welcome/greet" ומחזירה טקסט פשוט

## קוד מינימלי

### קלאס האפליקציה

</div>

```java
package org.example.stage2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Stage2Application {

    public static void main(String[] args) {
        SpringApplication.run(Stage2Application.class, args);
    }
}
```

<div dir="rtl">

### קונטרולר בסיסי


</div>

```java
package org.example.stage2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    @ResponseBody
    @RequestMapping("/greet")
    public String greet() {
        return "welcome to the world of spring boot";
    }
}
```

<div dir="rtl">

## מה חסר לשלב הבא
בשלב זה עדיין חסרים:
1. מודלים שייצגו את הנתונים שלנו
2. שירותים שיכילו את הלוגיקה העסקית
3. REST API מלא המאפשר פעולות CRUD
4. שימוש באנוטציות מודרניות יותר כמו `@RestController`, `@GetMapping`, וכו'

כדי לעבור לשלב הבא, נצטרך להוסיף מודל נתונים, שירות, וקונטרולר מתקדם יותר.

</div>

