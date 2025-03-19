package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromGarageToTerminal1 {

    private final static List<Long> garageToTerminal1Route;

    static {

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(114L, integer -> ++integer)
                .limit(15)
                .toList();

        to.addAll(toLower);



        garageToTerminal1Route = new ArrayList<>(List.of(

                299L, 279L,
                269L, 224L,
                214L, 169L,
                24L, 25L, 26L, 27L
        ));

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(12)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(12)
                .toList();

        from.addAll(fromLower);

        garageToTerminal1Route.addAll(to);
        garageToTerminal1Route.addAll(from);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && garageToTerminal1Route.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> garageToTerminal1Route.contains(graphPoint.getId());
    }

}
