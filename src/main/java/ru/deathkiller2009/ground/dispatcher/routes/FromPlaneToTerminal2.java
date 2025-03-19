package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromPlaneToTerminal2 {

    private final static List<Long> planeToTerminal2Route;

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


        planeToTerminal2Route = new ArrayList<>(
                List.of(29L, 30L, 31L, 32L)
        );

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(17)
                .toList();

        from.addAll(fromLower);

        planeToTerminal2Route.addAll(from);
        planeToTerminal2Route.addAll(gates);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && planeToTerminal2Route.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> planeToTerminal2Route.contains(graphPoint.getId());
    }

}
