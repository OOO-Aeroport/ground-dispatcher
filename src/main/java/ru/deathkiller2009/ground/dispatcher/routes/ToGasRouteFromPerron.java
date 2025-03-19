package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ToGasRouteFromPerron {

    private final static List<Long> toGasRoute;

    private final static List<Long> gasPoints;

    static {
        Stream<Long> lower = Stream.iterate(1L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower2 = Stream.iterate(33L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower3 = Stream.iterate(65L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower4 = Stream.iterate(97L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower5 = Stream.iterate(129L, integer -> ++integer)
                .limit(15);
        Stream<Long> concated = Stream.concat(lower, lower2);
        Stream<Long> concated1 = Stream.concat(concated, lower3);
        Stream<Long> concated2 = Stream.concat(concated1, lower4);
        Stream<Long> concated3 = Stream.concat(concated2, lower5);
        List<Long> gates = concated3.toList();

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(17)
                .toList();

        from.addAll(fromLower);


        Stream<Long> up = Stream.iterate(310L, integer -> integer + 3)
                .limit(7);
        Stream<Long> up1 = Stream.iterate(311L, integer -> integer + 3)
                .limit(10);
        Stream<Long> up3 = Stream.iterate(312L, integer -> integer + 3)
                .limit(10);

        Stream<Long> concat = Stream.concat(up, up1);
        List<Long> upp = Stream.concat(concat, up3).toList();

        toGasRoute = new ArrayList<>();

        gasPoints = List.of(334L, 335L, 336L);

        toGasRoute.addAll(upp);
        toGasRoute.addAll(gates);
        toGasRoute.addAll(from);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || gasPoints.contains(graphPoint.getId())
                                                                                                              && toGasRoute.contains(graphPoint.getId()));
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint) {
        return graphPoint -> toGasRoute.contains(graphPoint.getId());
    }

}
