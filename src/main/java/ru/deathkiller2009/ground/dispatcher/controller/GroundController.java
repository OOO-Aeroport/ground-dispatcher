package ru.deathkiller2009.ground.dispatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.deathkiller2009.ground.dispatcher.logic.VehicleType;
import ru.deathkiller2009.ground.dispatcher.service.GroundService;

import java.util.List;

@RestController
public class GroundController {

    private final GroundService groundService;

    public GroundController(GroundService groundService) {
        this.groundService = groundService;
    }

    @GetMapping("/{current-point}/gas")
    public List<Long> getRouteGas(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForGas(initialPoint);
    }

    @GetMapping("/point/{current-point}/{target}")
    public ResponseEntity<Boolean> canGoToPoint(@PathVariable("current-point") long initialPoint, @PathVariable("target") int targetPoint) {
        return ResponseEntity.ok(groundService.checkIfCarCanGo(initialPoint, targetPoint));
    }

    @GetMapping("/{current-point}/terminal1")
    public List<Long> getRouteTerminal1(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForTerminal1(initialPoint);
    }

    @GetMapping("/{current-point}/terminal2")
    public List<Long> getRouteTerminal2(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForTerminal2(initialPoint);
    }

    @GetMapping("/{current-point}/luggage")
    public List<Long> getRouteLuggage(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForLuggage(initialPoint);
    }

    @GetMapping("/garage/{vehicleType}") //todo добавить тип машинок к url'ам?
    public Boolean canGetOutOfGarage(@PathVariable("vehicleType") VehicleType type) {
        return groundService.checkIfCarCanGetOutOfGarage(type); //todo Понять что дописать и дописать
    }

    @GetMapping("/plane/{current-point}/{planeId}")
    public List<Long> getRouteToPlane(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteForPlane(initialPoint, planeId);
    }

    //todo Метод на выезд из гаража

    @GetMapping("/{current-point}/garage")
    public List<Long> getBackToGarage(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForGarage(initialPoint);
    }

    //todo Тестировать маршруты с несколькими машинками / препятствиями

}
