package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Service
public class StudentService {
    private final HashMap<Long, Student> students = new HashMap<>();

    private final Set<Student> filteredStudent = new HashSet<>();
    private long lastId = 0;

//    public static void addStudent (Student student) {
//        students//закончил здесь
//    }

    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudentById(long id) {
        return students.get(id);
    }

    public Student editStudent(long id, Student student) {
        if (!students.containsKey(id)) {
            return null;
        }
        students.put(id, student);
        return student;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Set<Student> getStudentsByAge(int age) {
        filteredStudent.clear();
        for (Student student : students.values()) {
            if (student.getAge() == age) {
                filteredStudent.add(student);
            }
        }
        return filteredStudent;
    }

    public String getAllStudents() {
        return students.toString();
    }
}


