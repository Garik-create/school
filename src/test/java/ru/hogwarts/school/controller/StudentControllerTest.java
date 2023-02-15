package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    @SpyBean
    private FacultyController facultyController;

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudentTest() {
        Student student;
        student = new Student();
        student.setName("Ivan");
        student.setAge(33);
        student.setId(1L);

        ResponseEntity<Student> response = createStudentRequest(getUriBuilder().build().toUri(), student);
        Assertions.assertThat(response.getBody()).isEqualTo(student);
    }

    @Test
    public void getStudentByIdTest() {

        Student student;
        student = new Student();
        student.setName("Ivan");
        student.setAge(33);
        student.setId(1L);

        ResponseEntity<Student> response = createStudentRequest(getUriBuilder().build().toUri(), student);

        Student createdStudent = response.getBody();

        assert createdStudent != null;
        ResponseEntity<Student> responseById = getStudentByIdRequest(createdStudent.getId());

        Assertions
                .assertThat(createdStudent)
                .isEqualTo(responseById.getBody());
    }

    @Test
    public void getStudentsByAgeTest() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student1");
        student1.setAge(18);

        Student student2 = new Student();
        student2.setId(1L);
        student2.setName("Student2");
        student2.setAge(19);

        Student student3 = new Student();
        student3.setId(1L);
        student3.setName("Student3");
        student3.setAge(20);

        Student student4 = new Student();
        student4.setId(1L);
        student4.setName("Student4");
        student4.setAge(20);

        ResponseEntity<Student> response1 = createStudentRequest(getUriBuilder().build().toUri(), student1);
        ResponseEntity<Student> response2 = createStudentRequest(getUriBuilder().build().toUri(), student2);
        ResponseEntity<Student> response3 = createStudentRequest(getUriBuilder().build().toUri(), student3);
        ResponseEntity<Student> response4 = createStudentRequest(getUriBuilder().build().toUri(), student4);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("age", "20");

        var studentsByAgeRequest = getStudentsByAgeRequest(queryParams);

        Assertions
                .assertThat(studentsByAgeRequest.getBody())
                .isNotNull();
        Assertions
                .assertThat(studentsByAgeRequest.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(studentsByAgeRequest.getBody())
                .contains(student3, student4);
    }

    @Test
    public void findByAgeBetweenTest() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student1");
        student1.setAge(18);

        Student student2 = new Student();
        student2.setId(1L);
        student2.setName("Student2");
        student2.setAge(19);

        Student student3 = new Student();
        student3.setId(1L);
        student3.setName("Student3");
        student3.setAge(20);

        Student student4 = new Student();
        student4.setId(1L);
        student4.setName("Student4");
        student4.setAge(20);

        ResponseEntity<Student> response1 = createStudentRequest(getUriBuilder().build().toUri(), student1);
        ResponseEntity<Student> response2 = createStudentRequest(getUriBuilder().build().toUri(), student2);
        ResponseEntity<Student> response3 = createStudentRequest(getUriBuilder().build().toUri(), student3);
        ResponseEntity<Student> response4 = createStudentRequest(getUriBuilder().build().toUri(), student4);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("from", "18");
        queryParams.add("to", "21");

        var studentsBetweenAgeRequest = findByAgeBetweenRequest(queryParams);

        Assertions
                .assertThat(studentsBetweenAgeRequest.getBody())
                .isNotNull();
        Assertions
                .assertThat(studentsBetweenAgeRequest.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(studentsBetweenAgeRequest.getBody())
                .contains(student3, student4, student2);

    }

    @Test
    public void editStudentTest() {
        Student student;
        student = new Student();
        student.setName("Ivan");
        student.setAge(33);
        student.setId(1L);

        ResponseEntity<Student> response = createStudentRequest(getUriBuilder().build().toUri(), student);

        Student createdStudent = response.getBody();
        createdStudent.setAge(21);
        createdStudent.setName("George");

        restTemplate.put(getUriBuilder().build().toUri(), createdStudent);

        ResponseEntity<Student> updatedStudent = updatedStudentRequest(createdStudent);

        Assertions
                .assertThat(updatedStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(updatedStudent.getBody().getAge()).isEqualTo(21);
        Assertions
                .assertThat(updatedStudent.getBody().getName()).isEqualTo("George");

    }

    @Test
    public void deleteStudentTest() {
        Student student;
        student = new Student();
        student.setName("Ivan");
        student.setAge(33);
        student.setId(1L);

        ResponseEntity<Student> response = createStudentRequest(getUriBuilder().build().toUri(), student);

        Student createdStudent = response.getBody();

        assert createdStudent != null;
        deleteStudentByIdRequestTest(createdStudent);

        URI uri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> responseEmpty = restTemplate.getForEntity(uri, Student.class);

        Assertions
                .assertThat(responseEmpty.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void findFacultyByStudentIdTest() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("green");
        faculty.setName("seventh");

        Student student = new Student();
        student.setName("Ivan");
        student.setAge(33);
        student.setId(1L);
        student.setFaculty(faculty);

        facultyController.createFaculty(faculty);

//        var createdStudent = studentController.createStudent(student);

        var createdStudent = restTemplate.postForEntity(getUriBuilder().build().toUri(),
                student,
                Student.class);


        var facultyByStudentIdRequest = getFacultyByStudentIdRequest(
                Objects.requireNonNull(createdStudent.getBody()));

        Assertions
                .assertThat(createdStudent.getBody().getFaculty())
                .isEqualTo(facultyByStudentIdRequest.getBody());

    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("Http")
                .host("localhost")
                .port(port)
                .path("/student");
    }

    private UriComponentsBuilder getUriBuilder2() {
        return UriComponentsBuilder.newInstance()
                .scheme("Http")
                .host("localhost")
                .port(port)
                .path("/student/filteredByAgeRange");
    }

    private UriComponentsBuilder getUriBuilder3() {
        return UriComponentsBuilder.newInstance()
                .scheme("Http")
                .host("localhost")
                .port(port)
                .path("/student/faculty");

    }

    private ResponseEntity<Student> createStudentRequest(URI uri, Student student) {
        return restTemplate.postForEntity(uri, student, Student.class);
    }

    private ResponseEntity<Student> getStudentByIdRequest(Long studentId) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(studentId).toUri();
        return restTemplate.getForEntity(uri, Student.class);
    }

    private ResponseEntity<Collection<Student>> getStudentsByAgeRequest(
            MultiValueMap<String, String> queryParams) {
        URI uri = getUriBuilder().queryParams(queryParams).build().toUri();
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });

    }

    private ResponseEntity<Collection<Student>> findByAgeBetweenRequest(
            MultiValueMap<String, String> queryParams) {
        URI uri = getUriBuilder2()
                .queryParams(queryParams)
                .build()
                .toUri();
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
    }

    private ResponseEntity<Student> updatedStudentRequest(Student createdStudent) {
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        return restTemplate.getForEntity(getUri, Student.class);
    }

    private void deleteStudentByIdRequestTest(Student createdStudent) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        restTemplate.delete(uri);
    }

    private ResponseEntity<Faculty> getFacultyByStudentIdRequest(Student createdStudent) {
        URI uri = getUriBuilder3()
                .path("/{studentId}")
                .buildAndExpand(createdStudent.getFaculty().getId()).toUri();
        return restTemplate.getForEntity(uri, Faculty.class);
    }
}
