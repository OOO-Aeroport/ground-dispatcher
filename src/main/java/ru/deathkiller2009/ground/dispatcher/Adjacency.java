package ru.deathkiller2009.ground.dispatcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adjacency {

    private GraphPoint node;
    private GraphPoint neighbourNode;

}
