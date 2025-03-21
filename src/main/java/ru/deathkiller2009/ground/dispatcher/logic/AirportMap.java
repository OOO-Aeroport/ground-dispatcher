package ru.deathkiller2009.ground.dispatcher.logic;

import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.deathkiller2009.ground.dispatcher.Adjacency;
import ru.deathkiller2009.ground.dispatcher.MapDao;
import ru.deathkiller2009.ground.dispatcher.routes.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AirportMap {

    private final Graph<GraphPoint, DefaultEdge> map = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

    private final RestClient restClient;

    private List<GraphPoint> gas;

    private List<GraphPoint> terminal1;

    private List<GraphPoint> terminal2;

    private List<GraphPoint> garage;

    private List<GraphPoint> garageIn;

    private List<GraphPoint> luggage;

    private List<GraphPoint> planeParkSpot;

    private Map<Long, List<GraphPoint>> carStopPoints;

    private final MapDao mapDao;

    private List<Adjacency> edges;

    private List<GraphPoint> runway1;

    private List<GraphPoint> runway2;

    private Map<Long, GraphPoint> followMePoints;

    private Map<Long, GraphPoint> secondaryFollowMePoints;

    private final ToGasRoute toGasRoute = new ToGasRoute();

    private final FromGasToPlane fromGasToPlane = new FromGasToPlane();

    private final FromPerronToGarage fromPerronToGarage = new FromPerronToGarage();

    private final ToGasRouteFromPerron toGasRouteFromPerron = new ToGasRouteFromPerron();

    private final FromGarageToLuggage fromGarageToLuggage = new FromGarageToLuggage();

    private final FromPlaneToLuggage fromPlaneToLuggage = new FromPlaneToLuggage();

    private final FromLuggageToPlane fromLuggageToPlane = new FromLuggageToPlane();

    private final FromGarageToPlane fromGarageToPlane = new FromGarageToPlane();

    private final FromGarageToTerminal1 fromGarageToTerminal1 = new FromGarageToTerminal1();

    private final FromTerminal1ToPlane fromTerminal1ToPlane = new FromTerminal1ToPlane();

    private final FromGarageToTerminal2 fromGarageToTerminal2 = new FromGarageToTerminal2();

    private final FromTerminal2ToPlane fromTerminal2ToPlane = new FromTerminal2ToPlane();

    private final FromPlaneToTerminal1 fromPlaneToTerminal1 = new FromPlaneToTerminal1();

    private final FromPlaneToTerminal2 fromPlaneToTerminal2 = new FromPlaneToTerminal2();

    private final FromPerronToRunway fromPerronToRunway = new FromPerronToRunway();

    private final FromGarageToPlaneOnRunway fromGarageToPlaneOnRunway = new FromGarageToPlaneOnRunway();

    private final FromRunwayToPerron fromRunwayToPerron = new FromRunwayToPerron();

    public AirportMap(RestClient restClient, MapDao mapDao) {
        this.restClient = restClient;
        this.mapDao = mapDao;
    }

    @PostConstruct
    public void initMap() {
        Map<Long, GraphPoint> graphPoints = mapDao.getGraphPoints();
        gas = List.of(graphPoints.get(334L), graphPoints.get(335L), graphPoints.get(336L));
        terminal1 = List.of(graphPoints.get(24L), graphPoints.get(25L), graphPoints.get(26L), graphPoints.get(27L));
        terminal2 = List.of(graphPoints.get(29L), graphPoints.get(30L), graphPoints.get(31L), graphPoints.get(32L));
        garageIn = List.of(graphPoints.get(302L), graphPoints.get(301L));
        garage = List.of(graphPoints.get(300L), graphPoints.get(299L), graphPoints.get(298L), graphPoints.get(297L));
        luggage = List.of(graphPoints.get(17L), graphPoints.get(18L), graphPoints.get(19L), graphPoints.get(20L));
        planeParkSpot = List.of(graphPoints.get(100L), graphPoints.get(36L), graphPoints.get(105L), graphPoints.get(41L),
                graphPoints.get(46L), graphPoints.get(110L));
        carStopPoints = Map.of(
                100L, List.of(graphPoints.get(99L), graphPoints.get(101L)),
                36L, List.of(graphPoints.get(35L), graphPoints.get(37L)),
                105L, List.of(graphPoints.get(104L), graphPoints.get(106L)),
                41L, List.of(graphPoints.get(40L), graphPoints.get(42L)),
                110L, List.of(graphPoints.get(109L), graphPoints.get(111L)),
                46L, List.of(graphPoints.get(45L), graphPoints.get(47L))
        );

        runway1 = Stream.iterate(171L, integer -> integer + 1)
                .limit(40)
                .map(graphPoints::get)
                .toList();

        runway2 = Stream.iterate(226L, aLong -> aLong + 1)
                .limit(40)
                .map(graphPoints::get)
                .toList();

        followMePoints = Map.of(
                100L, graphPoints.get(68L),
                36L, graphPoints.get(4L),
                105L, graphPoints.get(73L),
                41L, graphPoints.get(9L),
                110L, graphPoints.get(78L),
                46L, graphPoints.get(14L)
        );

        secondaryFollowMePoints = Map.of(
                100L, graphPoints.get(132L),
                36L, graphPoints.get(68L),
                105L, graphPoints.get(137L),
                41L, graphPoints.get(73L),
                110L, graphPoints.get(142L),
                46L, graphPoints.get(78L)
        );

        graphPoints.values().forEach(map::addVertex);
        System.out.println(map);
        edges = mapDao.getEdges();
        edges.forEach(adjacency -> map.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));

//        GraphPoint plane = graphPoints.get(110L);
//        plane.setStatus(Status.OCCUPIED);
//        plane.setVehicleType(VehicleType.PLANE);
//        plane.setVehicleId(5L);

//        GraphPoint plane1 = graphPoints.get(195L);
//        plane1.setStatus(Status.OCCUPIED);
//        plane1.setVehicleType(VehicleType.PLANE);
//        plane1.setVehicleId(1313L);


//        restClient.post().uri("http://26.21.3.228:4444/update_position")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body("""
//                        {
//                            "points": [%d],
//                            "type": "%s"
//                        }
//                        """.formatted(plane1.getId(), plane1.getVehicleType().toString().toLowerCase()))
//                .retrieve().body(String.class);
//        gas.forEach(graphPoint -> graphPoint.setStatus(Status.OCCUPIED));
    }

    private synchronized List<GraphPoint> buildRoute(long initialPoint, long targetPoint) {
        GraphPath<GraphPoint, DefaultEdge> path = null;
        while (path == null) {
            Graph<GraphPoint, DefaultEdge> clearedGraph = removeObstacles(initialPoint);
            BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath = new BFSShortestPath<>(clearedGraph);
            Map<Long, GraphPoint> vertexes = clearedGraph.vertexSet().stream()
                    .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
            path = bfsShortestPath.getPath(vertexes.get(initialPoint), vertexes.get(targetPoint));
        }
        List<GraphPoint> vertexList = path.getVertexList();
        vertexList.removeFirst();
        return vertexList;
    }

    private synchronized Graph<GraphPoint, DefaultEdge> removeObstacles(long initialPoint) {
        Graph<GraphPoint, DefaultEdge> copy = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

        Map<Long, GraphPoint> filteredVertexes = map.vertexSet()
                .stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY
                                               || graphPoint.getStatus() == Status.CHOSEN_TO_BE_OCCUPIED ||
                                               graphPoint.getId() == initialPoint)
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));

        filteredVertexes.values().forEach(copy::addVertex);

        List<Adjacency> adjacencyList = edges.stream().filter(adjacency -> filteredVertexes.containsKey(adjacency.getNode().getId()) && filteredVertexes.containsKey(adjacency.getNeighbourNode().getId()))
                .toList();

        adjacencyList.forEach(adjacency -> copy.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));
        return copy;
    }

    private synchronized List<GraphPoint> buildRoute(long initialPoint, long targetPoint, Predicate<GraphPoint> predicate, Predicate<GraphPoint> predicate2) {
        GraphPath<GraphPoint, DefaultEdge> path;
        Graph<GraphPoint, DefaultEdge> clearedGraph = removeObstacles(initialPoint, predicate);
        BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath = new BFSShortestPath<>(clearedGraph);
        Map<Long, GraphPoint> vertexes = clearedGraph.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        path = bfsShortestPath.getPath(vertexes.get(initialPoint), vertexes.get(targetPoint));
        List<GraphPoint> vertexList;
        try {
            vertexList = path.getVertexList();
        } catch (NullPointerException e) {
            Graph<GraphPoint, DefaultEdge> clearedGraph1 = removeObstacles(predicate2);
            BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath1 = new BFSShortestPath<>(clearedGraph1);
            Map<Long, GraphPoint> vertexes1 = clearedGraph1.vertexSet().stream()
                    .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
            path = bfsShortestPath1.getPath(vertexes1.get(initialPoint), vertexes1.get(targetPoint));
            vertexList = path.getVertexList();
        }
        vertexList.removeFirst();
        return vertexList;
    }

    private synchronized Graph<GraphPoint, DefaultEdge> removeObstacles(Predicate<GraphPoint> predicate) {
        Graph<GraphPoint, DefaultEdge> copy = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

        Map<Long, GraphPoint> filteredVertexes = map.vertexSet()
                .stream().filter(predicate)
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));

        filteredVertexes.values().forEach(copy::addVertex);

        List<Adjacency> adjacencyList = edges.stream().filter(adjacency -> filteredVertexes.containsKey(adjacency.getNode().getId()) && filteredVertexes.containsKey(adjacency.getNeighbourNode().getId()))
                .toList();

        adjacencyList.forEach(adjacency -> copy.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));
        return copy;
    }

    private synchronized Graph<GraphPoint, DefaultEdge> removeObstacles(long initialPoint, Predicate<GraphPoint> predicate) {
        Graph<GraphPoint, DefaultEdge> copy = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

        Map<Long, GraphPoint> filteredVertexes = map.vertexSet()
                .stream().filter(predicate)
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));

        filteredVertexes.values().forEach(copy::addVertex);

        List<Adjacency> adjacencyList = edges.stream().filter(adjacency -> filteredVertexes.containsKey(adjacency.getNode().getId()) && filteredVertexes.containsKey(adjacency.getNeighbourNode().getId()))
                .toList();

        adjacencyList.forEach(adjacency -> copy.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));
        return copy;
    }

    public synchronized List<GraphPoint> buildRouteForGas(long initialPoint) {
        Optional<GraphPoint> gasPoint = gas.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return gasPoint.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), toGasRoute.getRoute(initialPoint), toGasRoute.getRouteAnyway(initialPoint))).orElse(buildRoute(initialPoint, gas.get(random.nextInt(0, garage.size() - 1)).getId(), toGasRoute.getRoute(initialPoint), toGasRoute.getRouteAnyway(initialPoint)));
    }

    public synchronized List<GraphPoint> buildRouteForGasFromPerron(long initialPoint) {
        Optional<GraphPoint> gasPoint = gas.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, gas.size() - 1);
        return gasPoint.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), toGasRouteFromPerron.getRoute(initialPoint), toGasRouteFromPerron.getRouteAnyway(initialPoint)))
                .orElse(buildRoute(initialPoint, gas.get(index).getId(), toGasRouteFromPerron.getRoute(initialPoint), toGasRouteFromPerron.getRouteAnyway(initialPoint)));
    }

    public synchronized List<GraphPoint> buildRouteForPlaneForFuelTruck(long initialPoint, long planeId) {
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId(), fromGasToPlane.getRoute(initialPoint, endPoint.getId()), fromGasToPlane.getRouteAnyway(initialPoint, planeId));
    }

    public synchronized List<GraphPoint> buildRouteForGarageFromPerron(long initialPoint) {
        Optional<GraphPoint> garagePoints = garageIn.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();

        int index = random.nextInt(0, garageIn.size() - 1);
        return garagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromPerronToGarage.getRoute(initialPoint, graphPoint.getId()), fromPerronToGarage.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, garageIn.get(index).getId(), fromPerronToGarage.getRoute(initialPoint, garageIn.get(index).getId()), fromPerronToGarage.getRouteAnyway(initialPoint, garageIn.get(index).getId())));
    }

    public synchronized List<GraphPoint> buildRouteForLuggageFromGarage(long initialPoint) {
        Optional<GraphPoint> luggagePoints = luggage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, luggage.size());
        return luggagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromGarageToLuggage.getRoute(initialPoint, graphPoint.getId()), fromGarageToLuggage.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, luggage.get(index).getId(), fromGarageToLuggage.getRoute(initialPoint, luggage.get(index).getId()), fromGarageToLuggage.getRouteAnyway(initialPoint, luggage.get(index).getId())));
    }

    public synchronized List<GraphPoint> buildRouteForLuggageFromPlane(long initialPoint) {
        Optional<GraphPoint> luggagePoints = luggage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, luggage.size());
        return luggagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromPlaneToLuggage.getRoute(initialPoint, graphPoint.getId()), fromPlaneToLuggage.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, luggage.get(index).getId(), fromPlaneToLuggage.getRoute(initialPoint, luggage.get(index).getId()), fromPlaneToLuggage.getRouteAnyway(initialPoint, luggage.get(index).getId())));
    }

    public synchronized List<GraphPoint> buildRouteForPlaneFromLuggage(long initialPoint, long planeId) {
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId(), fromLuggageToPlane.getRoute(initialPoint, endPoint.getId()), fromLuggageToPlane.getRouteAnyway(initialPoint, endPoint.getId()));
    }

    public synchronized List<GraphPoint> buildRouteForPlaneFromGarage(long initialPoint, long planeId) {
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId(), fromGarageToPlane.getRoute(initialPoint, endPoint.getId()), fromGarageToPlane.getRouteAnyway(initialPoint, endPoint.getId()));
    }

    public synchronized List<GraphPoint> buildRouteForTerminal1FromGarage(long initialPoint) {
        Optional<GraphPoint> terminal1Points = terminal1.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, terminal1.size());
        return terminal1Points.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromGarageToTerminal1.getRoute(initialPoint, graphPoint.getId()), fromGarageToTerminal1.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, terminal1.get(index).getId(), fromGarageToTerminal1.getRoute(initialPoint, terminal1.get(index).getId()), fromGarageToTerminal1.getRouteAnyway(initialPoint, terminal1.get(index).getId())));
    }

    public synchronized List<GraphPoint> buildRouteForPlaneFromTerminal1(long initialPoint, long planeId) {
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId(), fromTerminal1ToPlane.getRoute(initialPoint, endPoint.getId()), fromTerminal1ToPlane.getRouteAnyway(initialPoint, endPoint.getId()));
    }

    public synchronized List<GraphPoint> buildRouteForTerminal1FromPlane(long initialPoint) {
        Optional<GraphPoint> luggagePoints = terminal1.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, terminal1.size());
        return luggagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromPlaneToTerminal1.getRoute(initialPoint, graphPoint.getId()), fromPlaneToTerminal1.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, terminal1.get(index).getId(), fromPlaneToTerminal1.getRoute(initialPoint, terminal1.get(index).getId()), fromPlaneToTerminal1.getRouteAnyway(initialPoint, terminal1.get(index).getId())));
    }

    public synchronized List<GraphPoint> buildRouteForTerminal2FromGarage(long initialPoint) {
        Optional<GraphPoint> terminal1Points = terminal2.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, terminal2.size());
        return terminal1Points.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromGarageToTerminal2.getRoute(initialPoint, graphPoint.getId()), fromGarageToTerminal2.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, terminal2.get(index).getId(), fromGarageToTerminal2.getRoute(initialPoint, terminal2.get(index).getId()), fromGarageToTerminal2.getRouteAnyway(initialPoint, terminal2.get(index).getId()))); //todo Исправить!!!
    }

    public synchronized List<GraphPoint> buildRouteForPlaneFromTerminal2(long initialPoint, long planeId) {
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId(), fromTerminal2ToPlane.getRoute(initialPoint, endPoint.getId()), fromTerminal2ToPlane.getRouteAnyway(initialPoint, endPoint.getId()));
    }

    public synchronized List<GraphPoint> buildRouteForTerminal2FromPlane(long initialPoint) {
        Optional<GraphPoint> luggagePoints = terminal2.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        int index = random.nextInt(0, terminal2.size());
        return luggagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId(), fromPlaneToTerminal2.getRoute(initialPoint, graphPoint.getId()), fromPlaneToTerminal2.getRouteAnyway(initialPoint, graphPoint.getId())))
                .orElse(buildRoute(initialPoint, terminal2.get(index).getId(), fromPlaneToTerminal2.getRoute(initialPoint, terminal2.get(index).getId()), fromPlaneToTerminal2.getRouteAnyway(initialPoint, terminal2.get(index).getId()))); //todo Исправить!!!
    }



    public synchronized List<GraphPoint> buildRouteForTakeoff(long planeId) { //todo Сделать невозможным заезд на полосу если на ней самолет
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId).findFirst().get();

        if (runway1.stream().allMatch(graphPoint -> (graphPoint.getStatus() == Status.EMPTY && graphPoint.getVehicleId() == 0L) || graphPoint.getVehicleId().equals(plane.getVehicleId()))) {
            runway1.forEach(graphPoint -> graphPoint.setVehicleId(planeId));
            return buildRoute(plane.getId(), 171L, fromPerronToRunway.getRoute(plane.getId(), 171L), fromPerronToRunway.getRouteAnyway(plane.getId(), 171L));
        } else if (runway2.stream().allMatch(graphPoint -> (graphPoint.getStatus() == Status.EMPTY && graphPoint.getVehicleId() == 0L) || graphPoint.getVehicleId().equals(plane.getVehicleId()))) {
            runway2.forEach(graphPoint -> graphPoint.setVehicleId(planeId));
            return buildRoute(plane.getId(), 226L, fromPerronToRunway.getRoute(plane.getId(), 226L), fromPerronToRunway.getRouteAnyway(plane.getId(), 226L));
        }

        return List.of();
    }

    public synchronized List<GraphPoint> buildRouteForPlaneOnRunway(long initialPoint, long planeId) {
        Optional<GraphPoint> graphPointOptional1 = runway1.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst();
        Optional<GraphPoint> graphPointOptional = runway2.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst();
        GraphPoint planePosition = null;
        if (graphPointOptional.isPresent()) {
            planePosition = graphPointOptional.get();
        } else if (graphPointOptional1.isPresent()) {
            planePosition = graphPointOptional1.get();
        }
        return buildRoute(initialPoint, planePosition.getId() + 1, fromGarageToPlaneOnRunway.getRoute(initialPoint, planePosition.getId() + 1),
                fromGarageToPlaneOnRunway.getRouteAnyway(initialPoint, planePosition.getId() + 1));
    }

    public synchronized List<GraphPoint> buildRouteForParkingSpots(long initialPoint, long targetPoint) {
        List<GraphPoint> route = buildRoute(initialPoint, targetPoint, fromRunwayToPerron.getRoute(initialPoint, targetPoint),
                fromRunwayToPerron.getRoute(initialPoint, targetPoint));
        GraphPoint followMeStop = followMePoints.get(targetPoint);
        GraphPoint followMeStopBackUp = secondaryFollowMePoints.get(targetPoint);
        if (route.get(route.size() - 2).equals(followMeStop)) {
            route.add(followMeStopBackUp);
        } else if (route.get(route.size() - 2).equals(followMeStopBackUp)) {
            route.add(followMeStop);
        } else {
            route.add(followMeStop);
        }
        return route;
    }

    public synchronized boolean checkIfCarCanGo(long initialPoint, long targetPoint) { //todo Добавить потокобезопасность
        Map<Long, GraphPoint> pointMap = map.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        GraphPoint initial = pointMap.get(initialPoint);
        GraphPoint target = pointMap.get(targetPoint);
        if (target.getStatus() == Status.EMPTY) {
            initial.setStatus(Status.EMPTY);
            target.setStatus(Status.OCCUPIED);
            target.setVehicleType(initial.getVehicleType());
            target.setVehicleId(initial.getVehicleId());
            initial.setVehicleType(VehicleType.NONE);
            initial.setVehicleId(0L);
            restClient.post().uri("http://26.21.3.228:4444/update_position")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""
                            {
                                "points": [%d, %d],
                                "type": "%s"
                            }
                            """.formatted(initialPoint, targetPoint, target.getVehicleType().toString().toLowerCase()))
                    .retrieve().body(String.class);
            System.out.println(initialPoint + " " + " " + targetPoint + " " + target.getVehicleType());
            return true;
        }
        return false;
    }

    public synchronized boolean checkIfCarCanGetOutOfGarage(VehicleType type) { //todo В конце машинка должна исчезать с поля
        boolean canGo = false;
        if (Objects.requireNonNull(type) == VehicleType.FUEL_TRUCK) {
            canGo = garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 300L && graphPoint.getStatus() == Status.EMPTY);
            if (canGo) {
                GraphPoint point = garage.stream().filter(graphPoint -> graphPoint.getId() == 300L).findFirst().get();
                point.setStatus(Status.OCCUPIED);
                point.setVehicleType(type);
                restClient.post().uri("http://26.21.3.228:4444/update_position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "points": [%d],
                                    "type": "%s"
                                }
                                """.formatted(point.getId(), point.getVehicleType().toString().toLowerCase()))
                        .retrieve().body(String.class);
            }
            return canGo;
        } else if (type == VehicleType.FOLLOW_ME) {
            canGo = garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 297L && graphPoint.getStatus() == Status.EMPTY);
            if (canGo) {
                GraphPoint point = garage.stream().filter(graphPoint -> graphPoint.getId() == 297L).findFirst().get();
                point.setStatus(Status.OCCUPIED);
                point.setVehicleType(type);
                restClient.post().uri("http://26.21.3.228:4444/update_position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "points": [%d],
                                    "type": "%s"
                                }
                                """.formatted(point.getId(), point.getVehicleType().toString().toLowerCase()))
                        .retrieve().body(String.class);
            }
            return canGo;
        } else if (type == VehicleType.FOOD_TRUCK) {
            canGo = garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 298L && graphPoint.getStatus() == Status.EMPTY);
            if (canGo) {
                GraphPoint point = garage.stream().filter(graphPoint -> graphPoint.getId() == 298L).findFirst().get();
                point.setStatus(Status.OCCUPIED);
                point.setVehicleType(type);
                restClient.post().uri("http://26.21.3.228:4444/update_position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "points": [%d],
                                    "type": "%s"
                                }
                                """.formatted(point.getId(), point.getVehicleType().toString().toLowerCase()))
                        .retrieve().body(String.class);
            }
        } else if (type == VehicleType.LUGGAGE_CAR) {
            canGo = garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 299L && graphPoint.getStatus() == Status.EMPTY);
            if (canGo) {
                GraphPoint point = garage.stream().filter(graphPoint -> graphPoint.getId() == 299L).findFirst().get();
                point.setStatus(Status.OCCUPIED);
                point.setVehicleType(type);
                restClient.post().uri("http://26.21.3.228:4444/update_position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "points": [%d],
                                    "type": "%s"
                                }
                                """.formatted(point.getId(), point.getVehicleType().toString().toLowerCase()))
                        .retrieve().body(String.class);
            }

        } else if (type == VehicleType.PASSENGER_BUS) {
            canGo = garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 299L && graphPoint.getStatus() == Status.EMPTY);
            if (canGo) {
                GraphPoint point = garage.stream().filter(graphPoint -> graphPoint.getId() == 299L).findFirst().get();
                point.setStatus(Status.OCCUPIED);
                point.setVehicleType(type);
                restClient.post().uri("http://26.21.3.228:4444/update_position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "points": [%d],
                                    "type": "%s"
                                }
                                """.formatted(point.getId(), point.getVehicleType().toString().toLowerCase()))
                        .retrieve().body(String.class);
            }

        }
        return canGo;
    }

    public synchronized List<Long> checkIfPlaneCanLand(long planeId) { //-1 если нельзя!
        List<GraphPoint> park = planeParkSpot.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .toList();
        Random random = new Random();
        if (park.isEmpty() || (runway1.stream().anyMatch(graphPoint -> graphPoint.getStatus() == Status.OCCUPIED || graphPoint.getVehicleId() != 0L) &&
                               runway2.stream().anyMatch(graphPoint -> graphPoint.getStatus() == Status.OCCUPIED || graphPoint.getVehicleId() != 0L))) {
            return List.of(-1L);
        }

        GraphPoint spawnPlane;

        if (runway1.stream().allMatch(graphPoint -> graphPoint.getStatus() == Status.EMPTY && graphPoint.getVehicleId() == 0L)) {
            spawnPlane = runway1.get(random.nextInt(1, (runway1.size() - 1) / 2));
            spawnPlane.setStatus(Status.OCCUPIED);
            spawnPlane.setVehicleType(VehicleType.PLANE);
            spawnPlane.setVehicleId(planeId);
            restClient.post().uri("http://26.21.3.228:4444/update_position")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""
                            {
                                "points": [%d],
                                "type": "%s"
                            }
                            """.formatted(spawnPlane.getId(), spawnPlane.getVehicleType().toString().toLowerCase()))
                    .retrieve().body(String.class);
        } else if (runway2.stream().allMatch(graphPoint -> graphPoint.getStatus() == Status.EMPTY && graphPoint.getVehicleId() == 0L)) {
            spawnPlane = runway2.get(random.nextInt(1, (runway1.size() - 1) / 2));
            spawnPlane.setStatus(Status.OCCUPIED);
            spawnPlane.setVehicleType(VehicleType.PLANE);
            spawnPlane.setVehicleId(planeId);
            restClient.post().uri("http://26.21.3.228:4444/update_position")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""
                            {
                                "points": [%d],
                                "type": "%s"
                            }
                            """.formatted(spawnPlane.getId(), spawnPlane.getVehicleType().toString().toLowerCase()))
                    .retrieve().body(String.class);
        } else {
            return List.of(-1L);
        }

        int bound = park.size() - 1 == 0? 1: park.size() - 1;
        GraphPoint graphPoint = park.get(random.nextInt(0, bound));
        graphPoint.setStatus(Status.CHOSEN_TO_BE_OCCUPIED);
        return List.of(spawnPlane.getId(), graphPoint.getId());
    }

    public synchronized Boolean checkIfCarCanGo(long initialPoint, long targetPoint, long planePosition) {
        Map<Long, GraphPoint> pointMap = map.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        GraphPoint initial = pointMap.get(initialPoint);
        GraphPoint target = pointMap.get(targetPoint);
        GraphPoint plane = pointMap.get(planePosition);
        if (target.getStatus() == Status.EMPTY || target.getStatus() == Status.CHOSEN_TO_BE_OCCUPIED) {
            plane.setStatus(Status.EMPTY);
            initial.setVehicleId(plane.getVehicleId());
            plane.setVehicleId(0L);
            plane.setVehicleType(VehicleType.NONE);
            initial.setVehicleType(VehicleType.PLANE);
            target.setStatus(Status.OCCUPIED);
            target.setVehicleType(VehicleType.FOLLOW_ME);
            restClient.post().uri("http://26.21.3.228:4444/update_position")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""
                            {
                                "points": [%d, %d],
                                "type": "%s"
                            }
                            """.formatted(initialPoint, targetPoint, target.getVehicleType().toString().toLowerCase()))
                    .retrieve().body(String.class);

            restClient.post().uri("http://26.21.3.228:4444/update_position")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""
                            {
                                "points": [%d, %d],
                                "type": "%s"
                            }
                            """.formatted(planePosition, initialPoint, initial.getVehicleType().toString().toLowerCase()))
                    .retrieve().body(String.class);
            System.out.println(initialPoint + " " + " " + targetPoint + " " + target.getVehicleType());
            System.out.println(planePosition + " " + " " + initialPoint + " " + initial.getVehicleType());
            return true;
        }
        return false;
    }

    public synchronized void goToGarage(long endPoint) {
        GraphPoint point = map.vertexSet().stream().filter(graphPoint -> graphPoint.getId() == endPoint)
                .findFirst().get();
        point.setStatus(Status.EMPTY);
        point.setVehicleId(0L);
        point.setVehicleType(VehicleType.NONE);
    }

    public synchronized void takeoff(long point) {
        GraphPoint end = map.vertexSet().stream().filter(graphPoint -> graphPoint.getId() == point)
                .findFirst().get();
        end.setStatus(Status.EMPTY);
        end.setVehicleId(0L);
        end.setVehicleType(VehicleType.NONE);
        if (runway1.contains(end)) {
            runway1.forEach(graphPoint -> graphPoint.setVehicleId(0L));
        } else if (runway2.contains(end)) {
            runway2.forEach(graphPoint -> graphPoint.setVehicleId(0L));
        }
    }


}
