package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    private final List<Faculty> facultyList = new ArrayList<>();

    public Faculty createFaculty(Faculty faculty) {

        logger.debug("'createFaculty' method was requested with faculty {}:", faculty);

        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(long id) {

        logger.debug("'getFacultyById' method was requested with Id {}:", id);

        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {

        logger.debug("'editFaculty' method was requested with faculty {}:", faculty);

        return facultyRepository.save(faculty);
    }

    public void deleteFacultyById(long id) {

        logger.debug("'deleteFacultyById' method was requested with id {}:", id);

        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultyByColor(String color) {

        logger.debug("'getFacultyByColor' method was requested with color {}:", color);

        facultyList.clear();
        var allFaculties = getAllFaculties();
        for (Faculty faculty : allFaculties) {
            if (faculty.getColor().equals(color)) {
                facultyList.add(faculty);
            }
        }
        return facultyList;
    }

    public List<Faculty> getAllFaculties() {

        logger.debug("'getAllFaculties' method was requested");

        return facultyRepository.findAll();
    }

    public Collection<Faculty> findFacultyByColorOrByNameIgnoreCase(String color, String name) {

        logger.debug("'findFacultyByColorOrByNameIgnoreCase' method was requested with color - {} and name - {}:", color, name);

        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }


    public Collection<Student> getStudents(long id) {

        logger.debug("'getStudents' method was requested with id {}:", id);

        return facultyRepository.findById(id).orElseThrow().getStudents();
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("not found");
    }
}

