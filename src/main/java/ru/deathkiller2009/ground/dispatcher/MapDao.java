package ru.deathkiller2009.ground.dispatcher;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;
import ru.deathkiller2009.ground.dispatcher.logic.VehicleType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class MapDao implements RowMapper<GraphPoint> {

    private final JdbcTemplate jdbcTemplate;

    private Map<Long, GraphPoint> graphPointMap;

    public MapDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Map<Long, GraphPoint> getGraphPoints() {
        graphPointMap = jdbcTemplate.query("SELECT * FROM graph_point", this)
                .stream()
                .collect(Collectors.toMap(GraphPoint::getId, Function.identity()));

        return graphPointMap;
    }

    @Override
    public GraphPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
        String vehicleType = rs.getString("vehicle_type");
        return new GraphPoint(rs.getLong("id"),
                Status.valueOf(rs.getString("status")),
                rs.getLong("vehicle_id"),
                vehicleType == null? null: VehicleType.valueOf(vehicleType));
    }

    @Transactional
    public List<Adjacency> getEdges() {
        return jdbcTemplate.query("SELECT * FROM adjacency_list", new AdjacencyRowMapper());
    }

    class AdjacencyRowMapper implements RowMapper<Adjacency> {
        @Override
        public Adjacency mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Adjacency(graphPointMap.get(rs.getLong("node_id")), graphPointMap.get(rs.getLong("neighbor_id")));
        }
    }
}
