package ru.deathkiller2009.ground.dispatcher.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/plane/{current-point}/gas")
    public List<Long> getRouteGasFromPerron(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForGasFromPerron(initialPoint);
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

    @GetMapping("plane/{current-point}/terminal1")
    public List<Long> getRouteTerminal1FromPlane(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForTerminal1FromPlane(initialPoint);
    }

    @GetMapping("plane/{current-point}/terminal2")
    public List<Long> getRouteTerminal2FromPlane(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForTerminal2FromPlane(initialPoint);
    }

    @GetMapping("/plane/terminal1/{current-point}/{planeId}")
    public List<Long> getRouteToPlaneFromTerminal1(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteToPlaneFromTerminal1(initialPoint, planeId);
    }

    @GetMapping("/plane/terminal2/{current-point}/{planeId}")
    public List<Long> getRouteToPlaneFromTerminal2(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteToPlaneFromTerminal2(initialPoint, planeId);
    }

    @GetMapping("/{current-point}/luggage")
    public List<Long> getRouteLuggageFromGarage(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForLuggageFromGarage(initialPoint);
    }

    @GetMapping("/plane/{current-point}/luggage")
    public List<Long> getRouteLuggageFromPlane(@PathVariable("current-point") long initialPoint) {
        return groundService.buildRouteForLuggageFromPlane(initialPoint);
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

    @GetMapping("/plane/fueltruck/{current-point}/{planeId}")
    public List<Long> getRouteToPlaneForFuel(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteForPlaneForFuel(initialPoint, planeId);
    }

    @GetMapping("/plane/luggage/{current-point}/{planeId}")
    public List<Long> getRouteToPlaneForLuggage(@PathVariable("current-point") long initialPoint, @PathVariable("planeId") long planeId) {
        return groundService.buildRouteForPlaneForLuggage(initialPoint, planeId);
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

    //todo Написать метод для удаления машинки с графа - помещение её в гараж

    @DeleteMapping("/garage/free/{endPoint}")
    public void deleteCar(@PathVariable("endPoint") long point) {
        groundService.goToGarage(point);
    }

    //todo Написать метод для удаления самолета - он в вк

    @DeleteMapping("/plane/takeoff/{endpoint}")
    public void deletePlane(@PathVariable("endpoint") long point) {
        groundService.takeoff(point);
    }

}
