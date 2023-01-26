package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    private final List<Faculty> facultyList = new ArrayList<>();

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFacultyById(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultyByColor(String color) {
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
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findFacultyByColorOrByNameIgnoreCase(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }


//    public Collection<Student> getStudents(long id) {
//        return facultyRepository.findById(id).get().getStudents().stream().toList();
//    }
}

