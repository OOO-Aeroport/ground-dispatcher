package ru.deathkiller2009.ground.dispatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.deathkiller2009.ground.dispatcher.logic.VehicleType;
import ru.deathkiller2009.ground.dispatcher.service.GroundService;

import java.util.List;

@RestController
@RequestMapping("/dispatcher")
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

    @GetMapping("/garage/{vehicleType}") //todo Спросить стоит ли добавлять id
    public Boolean canGetOutOfGarage(@PathVariable("vehicleType") String type) {
        VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
        return groundService.checkIfCarCanGetOutOfGarage(vehicleType);
    }

    @GetMapping("/plane/{current-point}/{planeId}")
    public List<Long> getRouteToPlane(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteForPlane(initialPoint, planeId);
    }

    @GetMapping("/plane/runway/{current-point}/{planeId}")
    public List<Long> getRouteToPlaneOnRunway(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteForPlaneOnRunway(initialPoint, planeId);
    }

    @GetMapping("/{current-point}/garage")
    public List<Long> getBackToGarage(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForGarage(initialPoint);
    }

    @GetMapping("/plane/{planeId}")
    public List<Long> canLand(@PathVariable("planeId") long planeId) {
        return groundService.canPlaneLand(planeId);
    }

    @GetMapping("/plane/follow-me/{initialPoint}/{targetPoint}")
    public List<Long> getRouteForParkingSpots(@PathVariable("initialPoint") long initialPoint, @PathVariable("targetPoint") long targetPoint) {
        return groundService.buildRouteForParkingSpots(initialPoint, targetPoint);
    }

    @GetMapping("/plane/follow-me/permission/{initialPoint}/{targetPoint}/{planeId}")
    public Boolean canGoToPoint(@PathVariable("initialPoint") long initialPoint,
                                @PathVariable("targetPoint") long targetPoint,
                                @PathVariable("planeId") long planeId) {
        return groundService.checkIfCarCanGo(initialPoint, targetPoint, planeId);
    }

    @GetMapping("/plane/takeoff/{planeId}")
    public List<Long> takeoffRoute(@PathVariable("planeId") long planeId) {
        return groundService.buildRouteForTakeoff(planeId);
    }


}
