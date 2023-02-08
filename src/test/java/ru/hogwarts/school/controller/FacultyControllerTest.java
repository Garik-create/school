package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void createFacultyTest() throws Exception {
        final String name = "sixth";
        final String color = "black";
        final long id = 1;

        var facultyObject = new JSONObject();//actual
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();//expected
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFacultyBiIdTest() throws Exception {
        final String name = "sixth";
        final String color = "black";
        final long id = 1;

//        var facultyObject = new JSONObject();//actual
//        facultyObject.put("id", id);
//        facultyObject.put("name", name);
//        facultyObject.put("color", color);

        Faculty faculty = new Faculty();//expected
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFacultyTest() throws Exception {

        final long id = 1;
        final String nameOld = "seventh";
        final String colorOld = "orange";

        final String nameNew = "ninth";
        final String colorNew = "blue";

        var facultyOld = new Faculty();
        facultyOld.setId(id);
        facultyOld.setName(nameOld);
        facultyOld.setColor(colorOld);

        facultyRepository.save(facultyOld);

        var facultyObject = new JSONObject();
        facultyObject.put("name", nameNew);
        facultyObject.put("color", colorNew);

        var facultyNew = new Faculty();
        facultyNew.setId(id);
        facultyNew.setName(nameNew);
        facultyNew.setColor(colorNew);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyNew);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(nameNew))
                .andExpect(jsonPath("$.color").value(colorNew));

    }

    @Test
    void deleteFacultyTest() throws Exception {
        final Long id = 1L;
        final String name = "sixth";
        final String color = "orange";

        var faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        facultyService.createFaculty(faculty);


        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.color").value(color));

        verify(facultyRepository, atLeastOnce()).deleteById(id);
    }


    @Test
    void getAllFacultiesTest() throws Exception {

        final long id1 = 1;
        final String name1 = "first";
        final String color1 = "green";

        final long id2 = 2;
        final String name2 = "second";
        final String color2 = "red";

        var faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        var faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyRepository.findAll()).thenReturn(List.of(faculty1, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));

    }

    @Test
    void findFacultyByColorOrByNameIgnoreCaseTest() throws Exception {

        final long id1 = 1;
        final long id2 = 2;
        final String name1 = "fiRst";
        final String name2 = "seCond";
        final String color = "grEen";


        var faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color);

        var faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color);

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name1))
                .thenReturn(List.of(faculty1, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/find")
                        .queryParam("color", color)
                        .queryParam("name", name1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));
    }

//    @Test
//    void findStudentsByFacultyId() throws Exception {
//
//        final long facultyId = 1;
//        final String name = "first";
//        final String color = "orange";
//
//        var faculty = new Faculty();
//        faculty.setId(facultyId);
//        faculty.setName(name);
//        faculty.setColor(color);
//
//        var student1 = new Student();
//        student1.setId(1);
//        student1.setFaculty(faculty);
//
//        var student2 = new Student();
//        student2.setId(2);
//        student2.setFaculty(faculty);
//
//        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
//
//        facultyService.getStudents(facultyId);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/faculty/students/{facultyId}", facultyId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(faculty)));
//    }
}