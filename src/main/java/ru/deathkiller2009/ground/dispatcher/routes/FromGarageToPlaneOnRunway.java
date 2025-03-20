package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromGarageToPlaneOnRunway {

    private final static List<Long> garageToPlaneOnRunwayRoute;

    static {

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(112L, integer -> ++integer)
                .limit(17)
                .toList();

        to.addAll(toLower);



        garageToPlaneOnRunwayRoute = new ArrayList<>(List.of(
                297L, 277L, 267L, 222L, 212L, 167L,
                165L, 166L, 211L, 220L, 221L, 266L, 275L, 276L,
                163L, 164L, 217L, 218L, 219L,
                272L, 273L, 274L
        ));

        Stream<Long> runway1 = Stream.iterate(171L, integer -> ++integer)
                .limit(40);
        Stream<Long> runway2 = Stream.iterate(226L, integer -> ++integer)
                .limit(40);

        List<Long> runways = Stream.concat(runway1, runway2).toList();

        garageToPlaneOnRunwayRoute.addAll(to);
        garageToPlaneOnRunwayRoute.addAll(runways);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && garageToPlaneOnRunwayRoute.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> garageToPlaneOnRunwayRoute.contains(graphPoint.getId());
    }

}
