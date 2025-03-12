package ru.deathkiller2009.ground.dispatcher.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.deathkiller2009.ground.dispatcher.service.GroundService;

@RestController
public class GroundController {

    private final GroundService groundService;

    public GroundController(GroundService groundService) {
        this.groundService = groundService;
    }


}
