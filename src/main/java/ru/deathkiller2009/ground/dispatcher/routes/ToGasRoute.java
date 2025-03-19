package ru.deathkiller2009.ground.dispatcher.routes;


import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.List;
import java.util.function.Predicate;

public class ToGasRoute  {

    private final static List<Long> gasRoute;

    private final static List<Long> gasPoints;

    static {
        gasRoute = List.of(
                300L, 280L, 270L,
                225L, 310L, 311L,
                312L, 334L, 335L,
                336L, 215L, 313L,
                314L, 315L, 170L,
                316L, 317L, 318L
        );
        gasPoints = List.of(334L, 335L, 336L);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || gasPoints.contains(graphPoint.getId())
        && gasRoute.contains(graphPoint.getId()));
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint) {
        return graphPoint -> gasRoute.contains(graphPoint.getId());
    }
}
