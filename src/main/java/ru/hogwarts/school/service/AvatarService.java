package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.*;
//import java.nio.file.Files;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${students.avatars.dir.path}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;

    private final StudentService studentService;

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }
//    private StudentRepository studentRepository;



    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {

        logger.debug("'uploadAvatar' method was requested for Student with Id: {}", studentId);

        Student student = studentService.getStudentById(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "."
                + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatarByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private Avatar findAvatarByStudentId(Long id) {

        logger.debug("'findAvatarByStudentId' method was requested for Student with Id: {}", id);

        return avatarRepository.findAvatarByStudent_Id(id).orElse(new Avatar());
    }

    public Avatar findAvatarByStudentId2(Long id) {

        logger.debug("'findAvatarByStudentId2' method was requested for Student with Id: {}", id);

        return avatarRepository.findAvatarByStudent_Id(id).orElse(new Avatar());
    }


    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {

        logger.debug("'getAllAvatars' method was requested with pageNumber - {} and pageSize - {}", pageNumber, pageSize);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
