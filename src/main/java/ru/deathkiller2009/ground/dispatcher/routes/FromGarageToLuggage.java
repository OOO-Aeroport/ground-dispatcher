package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromGarageToLuggage {

    private final static List<Long> garageToLuggageRoute;

    static {

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(112L, integer -> ++integer)
                .limit(17)
                .toList();

        to.addAll(toLower);



        garageToLuggageRoute = new ArrayList<>(List.of(
                298L, 278L,
                268L, 223L,
                213L, 168L,
                299L, 279L,
                269L, 224L,
                214L, 169L,
                17L, 18L, 19L, 20L
        ));

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(5)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(5)
                .toList();

        from.addAll(fromLower);

        garageToLuggageRoute.addAll(to);
        garageToLuggageRoute.addAll(from);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && garageToLuggageRoute.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> garageToLuggageRoute.contains(graphPoint.getId());
    }


}
