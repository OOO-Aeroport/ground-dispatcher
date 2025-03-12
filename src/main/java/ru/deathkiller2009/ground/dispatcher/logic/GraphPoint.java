package ru.deathkiller2009.ground.dispatcher.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphPoint {

    private long id;

    private Status status;

    private Long vehicleId;

    private VehicleType vehicleType;

    public GraphPoint(long id) {
        this.id = id;
        this.status = Status.EMPTY;
    }

}
