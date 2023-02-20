package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final Set<Student> filteredStudent = new HashSet<>();

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {

        logger.debug("'createStudent' method was requested with student {}:", student);

        return studentRepository.save(student);
    }

    public Student getStudentById(long id) {

        logger.debug("'getStudentById' method was requested with Id {}:", id);

        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {

        logger.debug("'editStudent' method was requested with student {}:", student);

        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {

        logger.debug("'deleteStudent' method was requested with Id {}:", id);

        studentRepository.deleteById(id);
    }

    public Set<Student> getStudentsByAge(int age) {

        logger.debug("'getStudentsByAge' method was requested with Age {}:", age);

        filteredStudent.clear();
        Collection<Student> students = getAllStudents();
        for (Student student : students) {
            if (student.getAge() == age) {
                filteredStudent.add(student);
            }
        }
        return filteredStudent;
    }

    public Collection<Student> getAllStudents() {

        logger.debug("'getAllStudents' method was requested");

        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int min, int max) {

        logger.debug("'findByAgeBetween' method was requested with min age - {} and max age - {}:", min, max);

        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFacultyByStudentId(long studentId) {

        logger.debug("'findFacultyByStudentId' method was requested with StudentId {}:", studentId);

        return studentRepository.findById(studentId).orElseThrow().getFaculty();
    }

    public Integer getAmountOfStudents() {

        logger.debug("'getAmountOfStudents' method was requested");

        return studentRepository.getAmountOfStudents();
    }

    public Double getAvgAgeOfStudents() {

        logger.debug("'getAvgAgeOfStudents' method was requested");

        return studentRepository.getAvgAgeOfStudents();
    }

    public Collection<Student> getLastStudents() {

        logger.debug("'getLastStudents' method was requested");

        return studentRepository.getLastStudents();
    }

    public List<String> getStudentsByLiteral(String liter) {
        String newLiter = "A";
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith(newLiter))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAvgAgeOfStudents2() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average().orElseThrow();
    }
}


