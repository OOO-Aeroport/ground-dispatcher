package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromPerronToRunway {

    private final static List<Long> perronToRunway;

    static {

        Stream<Long> lower = Stream.iterate(1L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower2 = Stream.iterate(33L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower3 = Stream.iterate(65L, integer -> ++integer)
                .limit(15);
        Stream<Long> lower4 = Stream.iterate(97L, integer -> ++integer)
                .limit(14);
        Stream<Long> lower5 = Stream.iterate(129L, integer -> ++integer)
                .limit(14);
        Stream<Long> concated = Stream.concat(lower, lower2);
        Stream<Long> concated1 = Stream.concat(concated, lower3);
        Stream<Long> concated2 = Stream.concat(concated1, lower4);
        Stream<Long> concated3 = Stream.concat(concated2, lower5);
        List<Long> gates = concated3.toList();


        Stream<Long> runway2 = Stream.iterate(171L, integer -> ++integer)
                .limit(40);
        Stream<Long> runway1 = Stream.iterate(226L, integer -> ++integer)
                .limit(40);

        List<Long> runways = Stream.concat(runway1, runway2).toList();


        perronToRunway = new ArrayList<>(
                List.of(161L, 216L, 217L, 218L, 219L)
        );

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(17)
                .toList();

        from.addAll(fromLower);


        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(112L, integer -> ++integer)
                .limit(17)
                .toList();

        to.addAll(toLower);

        perronToRunway.addAll(gates);
        perronToRunway.addAll(runways);
        perronToRunway.addAll(from);
        perronToRunway.addAll(to);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && perronToRunway.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> perronToRunway.contains(graphPoint.getId());
    }

}
