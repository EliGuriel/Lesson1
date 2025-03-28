package org.example.stage4.controller;


import org.example.stage4.model.Student;
import org.example.stage4.service.StudentService;
import org.example.stage4.service.StudentServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentServiceImpl.getAllStudents();
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestBody Student student) {
        return studentServiceImpl.addStudent(student);
    }

    @PutMapping("/updateStudent") // not good practice - should br use with id as well, path variable
    public String updateStudent(@RequestBody Student student) {
        return studentServiceImpl.updateStudent(student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentServiceImpl.deleteStudent(id);
    }
}
