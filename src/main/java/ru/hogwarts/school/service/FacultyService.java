package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    private List<Faculty> facultyList = new ArrayList<>();

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyById(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            return null;
        }
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty deleteFacultyById(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        for (Faculty faculty : faculties.values()) {
            if (faculty.getColor().equals(color)) {
                facultyList.add(faculty);
            }
        }
        return facultyList;
    }
}

