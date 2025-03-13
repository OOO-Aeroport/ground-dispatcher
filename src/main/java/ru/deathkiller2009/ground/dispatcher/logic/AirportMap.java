package ru.deathkiller2009.ground.dispatcher.logic;

import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;
import org.springframework.stereotype.Component;
import ru.deathkiller2009.ground.dispatcher.Adjacency;
import ru.deathkiller2009.ground.dispatcher.MapDao;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AirportMap {

    private final Graph<GraphPoint, DefaultEdge> map = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

    private List<GraphPoint> gas;

    private List<GraphPoint> terminal1;

    private List<GraphPoint> terminal2;

    private List<GraphPoint> garage;

    private List<GraphPoint> luggage;

    private List<GraphPoint> planeParkSpot;

    private Map<Long, List<GraphPoint>> carStopPoints;

    private final MapDao mapDao;

    private List<Adjacency> edges;

    private List<GraphPoint> runway1;

    private List<GraphPoint> runway2;

    public AirportMap(MapDao mapDao) {
        this.mapDao = mapDao;
    }

    @PostConstruct
    public void initMap() {
        Map<Long, GraphPoint> graphPoints = mapDao.getGraphPoints();
        gas = List.of(graphPoints.get(270L), graphPoints.get(225L), graphPoints.get(215L));
        terminal1 = List.of(graphPoints.get(24L), graphPoints.get(25L), graphPoints.get(26L), graphPoints.get(27L));
        terminal2 = List.of(graphPoints.get(29L), graphPoints.get(30L), graphPoints.get(31L), graphPoints.get(32L));
        garage = List.of(graphPoints.get(297L), graphPoints.get(298L), graphPoints.get(299L), graphPoints.get(300L));
        luggage = List.of(graphPoints.get(17L), graphPoints.get(18L), graphPoints.get(19L), graphPoints.get(20L));
        planeParkSpot = List.of(graphPoints.get(34L), graphPoints.get(38L), graphPoints.get(42L), graphPoints.get(46L),
                graphPoints.get(98L), graphPoints.get(102L), graphPoints.get(106L), graphPoints.get(110L));
        carStopPoints = Map.of(
                34L, List.of(graphPoints.get(33L), graphPoints.get(35L)),
                38L, List.of(graphPoints.get(37L), graphPoints.get(39L)),
                42L, List.of(graphPoints.get(41L), graphPoints.get(43L)),
                46L, List.of(graphPoints.get(45L), graphPoints.get(47L)),
                98L, List.of(graphPoints.get(97L), graphPoints.get(99L)),
                102L, List.of(graphPoints.get(101L), graphPoints.get(103L)),
                106L, List.of(graphPoints.get(105L), graphPoints.get(107L)),
                110L, List.of(graphPoints.get(109L), graphPoints.get(111L))
        );

        runway1 = Stream.iterate(171L, integer -> integer + 1)
                .limit(40)
                .map(graphPoints::get)
                .toList();

        runway2 = Stream.iterate(226L, aLong -> aLong + 1)
                .limit(40)
                .map(graphPoints::get)
                .toList();

        graphPoints.values().forEach(map::addVertex);
        System.out.println(map);
        edges = mapDao.getEdges();
        edges.forEach(adjacency -> map.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));

        GraphPoint plane = graphPoints.get(42L); //todo УДАЛИ ЭТО ДЛЯ ТЕСТА
        plane.setStatus(Status.OCCUPIED);
        plane.setVehicleType(VehicleType.PLANE);
        plane.setVehicleId(4L);

        GraphPoint car1 = graphPoints.get(224L);
        car1.setStatus(Status.OCCUPIED);
        car1.setVehicleType(VehicleType.FOOD_TRUCK);

        GraphPoint car2 = graphPoints.get(222L);
        car2.setStatus(Status.OCCUPIED);
        car2.setVehicleType(VehicleType.PASSENGER_BUS);

        GraphPoint car3 = graphPoints.get(223L);
        car3.setStatus(Status.OCCUPIED);
        car3.setVehicleType(VehicleType.FUEL_TRUCK);

        GraphPoint car4 = graphPoints.get(225L);
        car4.setStatus(Status.OCCUPIED);
        car4.setVehicleType(VehicleType.FUEL_TRUCK);

    }

    private List<GraphPoint> buildRoute(long initialPoint, long targetPoint) {
        Graph<GraphPoint, DefaultEdge> clearedGraph = removeObstacles(initialPoint);
        BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath = new BFSShortestPath<>(clearedGraph);
        Map<Long, GraphPoint> vertexes = clearedGraph.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        GraphPath<GraphPoint, DefaultEdge> path = bfsShortestPath.getPath(vertexes.get(initialPoint), vertexes.get(targetPoint));

        if (path == null) {

        }

        return path.getVertexList();
    }

    private Graph<GraphPoint, DefaultEdge> removeObstacles(long initialPoint) {

        Graph<GraphPoint, DefaultEdge> copy = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

        Map<Long, GraphPoint> filteredVertexes = map.vertexSet()
                .stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint)
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));

        filteredVertexes.values().forEach(copy::addVertex);

        List<Adjacency> adjacencyList = edges.stream().filter(adjacency -> filteredVertexes.containsKey(adjacency.getNode().getId()) && filteredVertexes.containsKey(adjacency.getNeighbourNode().getId()))
                .toList();

        adjacencyList.forEach(adjacency -> copy.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));

        return copy;
    }

    public List<GraphPoint> buildRouteForGas(long initialPoint) {
        Optional<GraphPoint> gasPoint = gas.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return gasPoint.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId())).orElse(buildRoute(initialPoint, gas.get(random.nextInt(0, garage.size() - 1)).getId()));
    }

    public boolean checkIfCarCanGo(long initialPoint, long targetPoint) { //todo Добавить потокобезопасность
        Map<Long, GraphPoint> pointMap = map.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        GraphPoint initial = pointMap.get(initialPoint);
        GraphPoint target = pointMap.get(targetPoint);
        if (target.getStatus() == Status.EMPTY) {
            initial.setStatus(Status.EMPTY);
            target.setStatus(Status.OCCUPIED);
            return true;
        }
        return false;
    }

    public List<GraphPoint> buildRouteForTerminal1(long initialPoint) {
        Optional<GraphPoint> terminal1Points = terminal1.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return terminal1Points.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId())).orElse(buildRoute(initialPoint, terminal1.get(random.nextInt(0, garage.size())).getId()));
    }

    public List<GraphPoint> buildRouteForTerminal2(long initialPoint) {
        Optional<GraphPoint> terminal2Points = terminal2.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return terminal2Points.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId()))
                .orElse(buildRoute(initialPoint, terminal2.get(random.nextInt(0, garage.size())).getId()));
    }

    public List<GraphPoint> buildRouteForLuggage(long initialPoint) {
        Optional<GraphPoint> luggagePoints = luggage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return luggagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId()))
                .orElse(buildRoute(initialPoint, luggage.get(random.nextInt(0, garage.size())).getId()));
    }

    public boolean checkIfCarCanGetOutOfGarage(VehicleType type) { //todo В конце машинка должна исчезать с поля

        if (Objects.requireNonNull(type) == VehicleType.FUEL_TRUCK) {
            return garage.stream().anyMatch(graphPoint -> graphPoint.getId() == 297L && graphPoint.getStatus() == Status.EMPTY);
        }
        return false;
    }

    public List<GraphPoint> buildRouteForGarage(long initialPoint) {
        Optional<GraphPoint> garagePoints = garage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return garagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId()))
                .orElse(buildRoute(initialPoint, garage.get(random.nextInt(0, garage.size())).getId()));
    }

    public List<GraphPoint> buildRouteForPlane(long initialPoint, long planeId) { //может ли быть такое, что точка не занята самолетом с указанным id?
        GraphPoint plane = planeParkSpot.stream().filter(graphPoint -> graphPoint.getVehicleId() == planeId)
                .findFirst().get();
        Random random = new Random();
        GraphPoint endPoint = carStopPoints.get(plane.getId()).stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny().orElse(carStopPoints.get(plane.getId()).get(random.nextInt(0, carStopPoints.get(plane.getId()).size() - 1)));
        return buildRoute(initialPoint, endPoint.getId());
    }
}
