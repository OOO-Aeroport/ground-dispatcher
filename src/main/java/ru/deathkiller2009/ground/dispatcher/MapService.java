package ru.deathkiller2009.ground.dispatcher;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.springframework.stereotype.Service;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.AirportMap;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class MapService {

    private final Graph<GraphPoint, DefaultEdge> map = new DefaultUndirectedGraph<>(DefaultEdge.class);

    private final MapDao mapDao;

    public MapService(MapDao mapDao) {
        this.mapDao = mapDao;
    }

    public void initMap() {
        Map<Long, GraphPoint> graphPoints = mapDao.getGraphPoints();
        graphPoints.values().forEach(map::addVertex);
        System.out.println(map);
        List<Adjacency> edges = mapDao.getEdges();
        edges.forEach(adjacency -> map.addEdge(adjacency.getNode(), adjacency.getNeighbourNode(), new DefaultEdge()));
        BFSShortestPath<GraphPoint, DefaultEdge> bfsShortestPath = new BFSShortestPath<>(map);
        GraphPath<GraphPoint, DefaultEdge> path =
                bfsShortestPath.getPath(graphPoints.get(210L), graphPoints.get(300L));
        System.out.println(path);
    }

}
