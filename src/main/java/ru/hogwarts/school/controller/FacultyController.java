package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyBiId(@PathVariable Long id) {
        var faculty = facultyService.getFacultyById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        var editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(@RequestParam String color) {
        if (color != null && !color.isBlank()) {
            var facultyByColor = facultyService.getFacultyByColor(color);
            return ResponseEntity.ok(facultyByColor);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        var allFaculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(allFaculties);
    }

    @GetMapping("/find")
    public ResponseEntity<Collection<Faculty>> findFacultyByColorOrByNameIgnoreCase(
            @RequestParam String color,
            @RequestParam String name) {
        if (color != null && !color.isBlank()
                && name != null && !name.isBlank()) {
            var facultyByColorOrByNameIgnoreCase =
                    facultyService.findFacultyByColorOrByNameIgnoreCase(color, name);
            return ResponseEntity.ok(facultyByColorOrByNameIgnoreCase);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    @GetMapping("/students/{facultyId}")
//    public ResponseEntity<Collection<Student>> findStudentsByFacultyId(
//            @PathVariable long facultyId) {
//        var students = facultyService.getStudents(facultyId);
//        return ResponseEntity.ok(students);
//    }
}
