package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromRunwayToPerron {

    private final static List<Long> runwayToPerron;

    static {

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(112L, integer -> ++integer)
                .limit(17)
                .toList();

        to.addAll(toLower);



        runwayToPerron = new ArrayList<>(List.of(
                297L, 277L, 267L, 222L, 212L, 167L,
                165L, 166L, 211L, 220L, 221L, 266L, 275L, 276L,
                163L, 164L, 216L, 217L, 218L, 219L, 271L,
                272L, 273L, 274L, 161L, 162L
        ));

        List<Long> up = Stream.iterate(281L, integer -> ++integer)
                .limit(16).toList();


        Stream<Long> runway1 = Stream.iterate(171L, integer -> ++integer)
                .limit(40);
        Stream<Long> runway2 = Stream.iterate(226L, integer -> ++integer)
                .limit(40);

        List<Long> runways = Stream.concat(runway1, runway2).toList();

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

        runwayToPerron.addAll(to);
        runwayToPerron.addAll(runways);
        runwayToPerron.addAll(up);
        runwayToPerron.addAll(gates);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && runwayToPerron.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> runwayToPerron.contains(graphPoint.getId());
    }

}
