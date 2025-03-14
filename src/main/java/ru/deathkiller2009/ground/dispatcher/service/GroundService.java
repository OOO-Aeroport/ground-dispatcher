package ru.deathkiller2009.ground.dispatcher.service;

import org.springframework.stereotype.Service;
import ru.deathkiller2009.ground.dispatcher.logic.AirportMap;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.VehicleType;

import java.util.List;

@Service
public class GroundService {

    private final AirportMap airportMap;

    public GroundService(AirportMap airportMap) {
        this.airportMap = airportMap;
    }

    public List<Long> buildRouteForGas(long initialPoint) {
        return airportMap.buildRouteForGas(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public boolean checkIfCarCanGo(long initialPoint, long targetPoint) {
        return airportMap.checkIfCarCanGo(initialPoint, targetPoint);
    }

    public List<Long> buildRouteForTerminal1(long initialPoint) {
        return airportMap.buildRouteForTerminal1(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForTerminal2(long initialPoint) {
        return airportMap.buildRouteForTerminal2(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForLuggage(long initialPoint) {
        return airportMap.buildRouteForLuggage(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public boolean checkIfCarCanGetOutOfGarage(VehicleType type) {
        return airportMap.checkIfCarCanGetOutOfGarage(type);
    }

    public List<Long> buildRouteForGarage(long initialPoint) {
        return airportMap.buildRouteForGarage(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForPlane(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlane(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForPlaneOnRunway(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneOnRunway(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> canPlaneLand(long planeId) {
        return airportMap.checkIfPlaneCanLand(planeId);
    }

    public List<Long> buildRouteForParkingSpots(long initialPoint, long targetPoint) {
        return airportMap.buildRouteForParkingSpots(initialPoint, targetPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public Boolean checkIfCarCanGo(long initialPoint, long targetPoint, long planeId) {
        return airportMap.checkIfCarCanGo(initialPoint, targetPoint, planeId);
    }

    public List<Long> buildRouteForTakeoff(long planeId) {
        return airportMap.buildRouteForTakeoff(planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }
}
