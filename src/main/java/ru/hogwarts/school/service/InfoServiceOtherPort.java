package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("otherPort")
public class InfoServiceOtherPort implements InfoService {

    @Value("${server.port}")
    private Integer serverPort;
    @Override
    public Integer getPort() {

        return serverPort;
    }
}
