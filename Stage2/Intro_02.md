<div dir="rtl">

### המשך הפיתוח - הוספת בקר בסיסי

</div>

```java
package org.example.firstmeeting1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Spring Boot";
    }
    
    @GetMapping("/info")
    public String info() {
        return "Spring Boot Server, running Embedded Tomcat Server on Port  8080";
    }
}
```

<div dir="rtl">

אחרי הוספת הבקר הבסיסי, אפשר להריץ מחדש את האפליקציה ולראות את התוצאות בדפדפן:
1. בנתיב שורש (`/`) תוצג ההודעה "Spring Boot"
2. בנתיב `/info` יוצגו פרטי השרת

</div>