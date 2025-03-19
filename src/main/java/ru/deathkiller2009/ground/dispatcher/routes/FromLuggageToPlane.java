package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromLuggageToPlane {

    private final static List<Long> luggageToPlane;

    static {

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(82L, integer -> ++integer)
                .limit(15)
                .toList());

        List<Long> fromLower = Stream.iterate(49L, integer -> ++integer)
                .limit(16)
                .toList();

        from.addAll(fromLower);

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(114L, integer -> ++integer)
                .limit(15)
                .toList();

        to.addAll(toLower);

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

        luggageToPlane = new ArrayList<>(List.of(
                17L, 18L, 19L, 20L
        ));

        luggageToPlane.addAll(from);
        luggageToPlane.addAll(to);
        luggageToPlane.addAll(gates);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && luggageToPlane.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> luggageToPlane.contains(graphPoint.getId());
    }

}
