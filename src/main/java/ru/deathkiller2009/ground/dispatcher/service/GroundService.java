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
        return airportMap.buildRouteForTerminal1FromGarage(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForTerminal2(long initialPoint) {
        return airportMap.buildRouteForTerminal2FromGarage(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

//    public List<Long> buildRouteForLuggage(long initialPoint) {
//        return airportMap.buildRouteForLuggage(initialPoint)
//                .stream().map(GraphPoint::getId)
//                .toList();
//    }

    public boolean checkIfCarCanGetOutOfGarage(VehicleType type) {
        return airportMap.checkIfCarCanGetOutOfGarage(type);
    }

    public List<Long> buildRouteForGarage(long initialPoint) {
        return airportMap.buildRouteForGarageFromPerron(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForPlane(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneFromGarage(initialPoint, planeId)
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

    public void goToGarage(long point) {
        airportMap.goToGarage(point);
    }

    public void takeoff(long point) {
        airportMap.takeoff(point);
    }

    public List<Long> buildRouteForPlaneForFuel(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneForFuelTruck(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForGasFromPerron(long initialPoint) {
        return airportMap.buildRouteForGasFromPerron(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForLuggageFromGarage(long initialPoint) {
        return airportMap.buildRouteForLuggageFromGarage(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForLuggageFromPlane(long initialPoint) {
        return airportMap.buildRouteForLuggageFromPlane(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForPlaneForLuggage(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneFromLuggage(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForTerminal1FromPlane(long initialPoint) {
        return airportMap.buildRouteForTerminal1FromPlane(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteForTerminal2FromPlane(long initialPoint) {
        return airportMap.buildRouteForTerminal2FromPlane(initialPoint)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteToPlaneFromTerminal1(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneFromTerminal1(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }

    public List<Long> buildRouteToPlaneFromTerminal2(long initialPoint, long planeId) {
        return airportMap.buildRouteForPlaneFromTerminal2(initialPoint, planeId)
                .stream().map(GraphPoint::getId)
                .toList();
    }
}
