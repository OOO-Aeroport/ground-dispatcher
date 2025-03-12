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
import ru.deathkiller2009.ground.dispatcher.logic.exception.NoSpaceException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AirportMap {

    private final Graph<GraphPoint, DefaultEdge> map = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

    private List<GraphPoint> gas;

    private List<GraphPoint> terminal1;

    private List<GraphPoint> terminal2;

    private List<GraphPoint> garage;

    private List<GraphPoint> luggage;

    private final MapDao mapDao;

    private List<Adjacency> edges;

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
        graphPoints.values().forEach(map::addVertex);
        System.out.println(map);
        edges = mapDao.getEdges();
        edges.forEach(adjacency -> map.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));
    }

    private List<GraphPoint> buildRoute(long initialPoint, long targetPoint) { //Todo Выбирать target точку
        Graph<GraphPoint, DefaultEdge> clearedGraph = removeObstacles();
        BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath = new BFSShortestPath<>(clearedGraph);
        Map<Long, GraphPoint> vertexes = clearedGraph.vertexSet().stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));
        GraphPath<GraphPoint, DefaultEdge> path = bfsShortestPath.getPath(vertexes.get(initialPoint), vertexes.get(targetPoint));
        return path.getVertexList();
    }

    private Graph<GraphPoint, DefaultEdge> removeObstacles() {

        Graph<GraphPoint, DefaultEdge> copy = new AsSynchronizedGraph<>(new DefaultUndirectedGraph<>(DefaultEdge.class));

        Map<Long, GraphPoint> filteredVertexes = map.vertexSet()
                .stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity())); //todo Initial Point может быть со статусом занято

        filteredVertexes.values().forEach(copy::addVertex);

        List<Adjacency> adjacencyList = edges.stream().filter(adjacency -> filteredVertexes.containsKey(adjacency.getNode().getId()) || filteredVertexes.containsKey(adjacency.getNeighbourNode().getId()))
                .toList();

        adjacencyList.forEach(adjacency -> copy.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));

        return copy;
    }

    public List<GraphPoint> buildRouteForGas(long initialPoint) {
        Optional<GraphPoint> gasPoint = gas.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return gasPoint.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId())).orElse(buildRoute(initialPoint, gas.get(random.nextInt(0, garage.size())).getId()));
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

    public Long checkIfCarCanGetOutOfGarage() {
        return garage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY).findAny()
                .map(graphPoint -> {
                    graphPoint.setStatus(Status.OCCUPIED);
                    return graphPoint.getId();
                }).orElseThrow();
    }

    public List<GraphPoint> buildRouteForGarage(long initialPoint) {
        Optional<GraphPoint> garagePoints = garage.stream().filter(graphPoint -> graphPoint.getStatus() == Status.EMPTY)
                .findAny();
        Random random = new Random();
        return garagePoints.map(graphPoint -> buildRoute(initialPoint, graphPoint.getId()))
                .orElse(buildRoute(initialPoint, garage.get(random.nextInt(0, garage.size())).getId()));
    }
}
