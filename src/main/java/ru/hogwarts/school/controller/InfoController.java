package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;
import ru.hogwarts.school.service.InfoServiceImpl;

import java.util.stream.Stream;

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

    @GetMapping("/int")
    public long getSum() {
        return Stream.iterate(1L, a -> a +1)
                .parallel()
                .limit(1_000_000)
                .reduce(0L, (a, b) -> a + b );
    }
}
