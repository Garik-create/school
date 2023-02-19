package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }


    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile avatarFile) throws IOException {
        if (avatarFile.getSize() > 1024 * 300) {
            ResponseEntity.badRequest().body("Fail is too big");
        }
        avatarService.uploadAvatar(studentId, avatarFile);
        return ResponseEntity.ok().body("Uploaded successfully");
    }

    @GetMapping("/{studentId}/avatar/preview")
    public ResponseEntity<byte[]> downLoadFromDatabase(@PathVariable Long studentId) {
        var avatar = avatarService.findAvatarByStudentId2(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("/{studentId}/avatar")
    public void downloadFromDisk(@PathVariable Long studentId,
                                 HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarByStudentId2(studentId);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }

    }

//    @GetMapping("/avatars")
//    public ResponseEntity<Collection<Avatar>> getAvatarsOfStudents(@RequestParam("page") Integer pageNumber,
//                                                                   @RequestParam("size") Integer pageSize) {
//        var avatarsOfStudents = avatarService.getAvatarsOfStudents(pageNumber, pageSize);
//        return ResponseEntity.ok(avatarsOfStudents);
//
//    }

    @GetMapping("/all-avatars")
    public ResponseEntity<List<Avatar>> getAllAvatars(@RequestParam("page") Integer pageNumber,
                                                      @RequestParam("size") Integer pageSize) {
        var allAvatarsByPages = avatarService.getAllAvatars(pageNumber, pageSize);
        return ResponseEntity.ok(allAvatarsByPages);
    }
}
