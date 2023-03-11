package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;
import ru.hogwarts.school.service.InfoServiceImpl;

@RestController
public class InfoController {
    public InfoController(InfoService infoService) {

        this.infoService = infoService;
    }


private final InfoService infoService;

    @GetMapping("/getPort")
    public ResponseEntity<Integer> getPort() {
        var port = infoService.getPort();
        return ResponseEntity.ok(port);
    }
}
