package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {

        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        var student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        var editedStudent = studentService.editStudent(student);
        if (editedStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentsByAge(
            @RequestParam(required = false) Integer age) {
        if (age != null) {
            var students = studentService.getStudentsByAge(age);
            if (students.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(students);
        }
        var allStudents = studentService.getAllStudents();
        return ResponseEntity.ok(allStudents);
    }

    @GetMapping("/filteredByAgeRange")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int from,
                                                                @RequestParam int to) {
        var ageBetween = studentService.findByAgeBetween(from, to);
        return ResponseEntity.ok(ageBetween);
    }


    @GetMapping("/faculty/{studentId}")
    public ResponseEntity<Faculty> findFacultyByStudentId(@PathVariable long studentId) {
        var faculty = studentService.findFacultyByStudentId(studentId);
        return ResponseEntity.ok(faculty);
    }
}
