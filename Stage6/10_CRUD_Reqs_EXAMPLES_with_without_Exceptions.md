# דוגמאות לשאילתות CRUD עם וללא חריגות

<div dir="rtl">

## 1. CREATE (POST) - הוספת סטודנט חדש

### דוגמה תקינה (ללא חריגות)

**בקשה**:
</div>

```http
POST /student/addStudent
Content-Type: application/json

{
  "id": 5,
  "firstName": "Sarah",
  "lastName": "Johnson",
  "age": 22.5
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 201 Created):
</div>

```json
{
  "id": 5,
  "firstName": "Sarah",
  "lastName": "Johnson",
  "age": 22.5
}
```

<div dir="rtl">
### דוגמאות עם חריגות

#### 1. שגיאת ולידציה - שם פרטי חסר (MethodArgumentNotValidException)

**בקשה**:
</div>

```http
POST /student/addStudent
Content-Type: application/json

{
  "id": 5,
  "firstName": "",
  "lastName": "Johnson",
  "age": 22.5
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 400 Bad Request):
</div>

```json
{
  "error": "Validation failed",
  "message": "{firstName=First name is required}",
  "timestamp": "2025-03-17T10:15:30.123"
}
```

<div dir="rtl">
#### 2. שגיאת ID כפול (IllegalArgumentException)

**בקשה**:
</div>

```http
POST /student/addStudent
Content-Type: application/json

{
  "id": 1,
  "firstName": "Sarah",
  "lastName": "Johnson",
  "age": 22.5
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 400 Bad Request):
</div>

```json
{
  "error": "Invalid input",
  "message": "Student with id 1 already exists",
  "timestamp": "2025-03-17T10:16:45.789"
}
```

<div dir="rtl">
#### 3. שגיאת ולידציה - גיל מתחת למינימום (MethodArgumentNotValidException)

**בקשה**:
</div>

```http
POST /student/addStudent
Content-Type: application/json

{
  "id": 5,
  "firstName": "Sarah",
  "lastName": "Johnson",
  "age": 15
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 400 Bad Request):
</div>

```json
{
  "error": "Validation failed",
  "message": "{age=Age should not be less than 16}",
  "timestamp": "2025-03-17T10:17:20.456"
}
```

<div dir="rtl">
## 2. READ (GET) - קבלת סטודנט או רשימת סטודנטים

### דוגמה תקינה - קבלת כל הסטודנטים (ללא חריגות)

**בקשה**:
</div>

```http
GET /student/getAllStudents
Accept: application/json
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 200 OK):
</div>

```json
[
  {
    "id": 1,
    "firstName": "Alice",
    "lastName": "Moskovitz",
    "age": 21.3
  },
  {
    "id": 2,
    "firstName": "Bob",
    "lastName": "Smith",
    "age": 22.3
  },
  {
    "id": 3,
    "firstName": "Charlie",
    "lastName": "Brown",
    "age": 23.3
  },
  {
    "id": 4,
    "firstName": "David",
    "lastName": "Miller",
    "age": 24.3
  }
]
```

<div dir="rtl">
### דוגמה תקינה - קבלת סטודנט לפי מזהה קיים (ללא חריגות)

**בקשה**:
</div>

```http
GET /student/1
Accept: application/json
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 200 OK):
</div>

```json
{
  "id": 1,
  "firstName": "Alice",
  "lastName": "Moskovitz",
  "age": 21.3
}
```

<div dir="rtl">
### דוגמה עם חריגה - מזהה לא קיים (NotFoundException)

**בקשה**:
</div>

```http
GET /student/100
Accept: application/json
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 404 Not Found):
</div>

```json
{
  "error": "Resource not found",
  "message": "Student with id 100 does not exist",
  "timestamp": "2025-03-17T10:20:15.789"
}
```

<div dir="rtl">
## 3. UPDATE (PUT) - עדכון סטודנט קיים

### דוגמה תקינה (ללא חריגות)

**בקשה**:
</div>

```http
PUT /student/updateStudent/3
Content-Type: application/json

{
  "id": 3,
  "firstName": "Charles",
  "lastName": "Brown",
  "age": 24.0
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 200 OK):
</div>

```json
{
  "id": 3,
  "firstName": "Charles",
  "lastName": "Brown",
  "age": 24.0
}
```

<div dir="rtl">
### דוגמאות עם חריגות

#### 1. מזהה לא קיים (NotFoundException)

**בקשה**:
</div>

```http
PUT /student/updateStudent/100
Content-Type: application/json

{
  "id": 100,
  "firstName": "Unknown",
  "lastName": "Person",
  "age": 25.0
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 404 Not Found):
</div>

```json
{
  "error": "Student not found",
  "message": "Student with id 100 does not exist",
  "timestamp": "2025-03-17T10:25:12.345"
}
```

<div dir="rtl">
#### 2. אימות ברמת השירות - שם פרטי ומשפחה זהים (IllegalArgumentException)

**בקשה**:
</div>

```http
PUT /student/updateStudent/3
Content-Type: application/json

{
  "id": 3,
  "firstName": "Brown",
  "lastName": "Brown",
  "age": 24.0
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 400 Bad Request):
</div>

```json
{
  "error": "Invalid data",
  "message": "First name and last name cannot be identical",
  "timestamp": "2025-03-17T10:26:45.678"
}
```

<div dir="rtl">
#### 3. ערכים חסרים (MethodArgumentNotValidException)

**בקשה**:
</div>

```http
PUT /student/updateStudent/3
Content-Type: application/json

{
  "id": 3,
  "firstName": null,
  "lastName": "Brown",
  "age": 24.0
}
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 400 Bad Request):
</div>

```json
{
  "error": "Validation failed",
  "message": "{firstName=First name is required}",
  "timestamp": "2025-03-17T10:27:30.123"
}
```

<div dir="rtl">
## 4. DELETE (DELETE) - מחיקת סטודנט

### דוגמה תקינה (ללא חריגות)

**בקשה**:
</div>

```http
DELETE /student/deleteStudent/4
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 204 No Content):
</div>

```
(גוף תגובה ריק)
```

<div dir="rtl">
### דוגמה עם חריגה - מזהה לא קיים (NotFoundException)

**בקשה**:
</div>

```http
DELETE /student/deleteStudent/100
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 404 Not Found):
</div>

```json
{
  "error": "Student not found",
  "message": "Student with id 100 does not exist",
  "timestamp": "2025-03-17T10:30:45.678"
}
```

<div dir="rtl">
## 5. דוגמה לחריגה כללית לא צפויה

במקרה של שגיאה כללית בשרת או חריגה לא צפויה:

**בקשה** (נניח שיש שגיאה בשרת):
</div>

```http
GET /student/getAllStudents
Accept: application/json
```

<div dir="rtl">
**תגובה** (קוד סטטוס: 500 Internal Server Error):
</div>

```json
{
  "error": "Internal server error",
  "message": "An unexpected error occurred while processing your request",
  "timestamp": "2025-03-17T10:35:12.345"
}
```

<div dir="rtl">
## איך לבדוק את הדוגמאות הללו

ניתן לבדוק את הדוגמאות הללו באמצעות:
1. **Postman**: כלי פשוט לשליחת בקשות HTTP
2. **HTTP Client של IntelliJ**: אם אתה משתמש ב-IntelliJ Ultimate
3. **curl**: כלי שורת פקודה לשליחת בקשות HTTP

### דוגמה לבדיקה ב-curl

בדיקת קבלת כל הסטודנטים:
</div>

```bash
curl -X GET http://localhost:8080/student/getAllStudents -H "Accept: application/json"
```

<div dir="rtl">
הוספת סטודנט חדש:
</div>

```bash
curl -X POST http://localhost:8080/student/addStudent \
     -H "Content-Type: application/json" \
     -d '{"id": 5, "firstName": "Sarah", "lastName": "Johnson", "age": 22.5}'
```

<div dir="rtl">
## סיכום סוגי החריגות ואופן הטיפול בהן

1. **MethodArgumentNotValidException** - נזרקת כאשר ולידציית Bean נכשלת (למשל, שם פרטי ריק או גיל מתחת למינימום)
2. **NotFoundException** - נזרקת כאשר מנסים לעדכן או למחוק סטודנט שאינו קיים
3. **IllegalArgumentException** - נזרקת כאשר יש בעיה לוגית בנתונים (כמו ID כפול או שם פרטי ומשפחה זהים)
4. **Exception** כללית - נתפסת במקרים של שגיאות כלליות או לא צפויות

כל החריגות הללו נתפסות על ידי ה-GlobalExceptionHandler ומתורגמות לתגובות HTTP מתאימות עם קודי סטטוס רלוונטיים ומידע שימושי בגוף התגובה.
</div>