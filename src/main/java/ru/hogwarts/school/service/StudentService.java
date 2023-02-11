package ru.hogwarts.school.service;

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


    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Set<Student> getStudentsByAge(int age) {
        filteredStudent.clear();
        Collection<Student> students = getAllStudents();
        for (Student student : students) {
            if (student.getAge()==age) {
                filteredStudent.add(student);
            }
        }
        return filteredStudent;
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

//    public Collection<Student> findByFacultyId(long facultyId) {
//        return studentRepository.findStudentsByFaculty_Id(facultyId);
//    }

    public Faculty findFacultyByStudentId(long studentId) {
        return studentRepository.findById(studentId).orElseThrow().getFaculty();
    }

    public Integer getAmountOfStudents() {
        return studentRepository.getAmountOfStudents();
    }

    public Integer getAvgAgeOfStudents() {
        return studentRepository.getAvgAgeOfStudents();
    }

    public List<Student> getStudentByLastIds() {
        return studentRepository.getStudentByLastIds();
    }
}


