package ru.deathkiller2009.ground.dispatcher.routes;

import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromGasToPlane {

    private final static List<Long> gasToPlaneRoute;



    static {

        List<Long> to = new java.util.ArrayList<>(Stream.iterate(144L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> toLower = Stream.iterate(112L, integer -> ++integer)
                .limit(17)
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

        gasToPlaneRoute = new ArrayList<>(List.of(
                334L, 335L, 336L,
                312L, 315L, 318L,
                319L, 320L, 321L, 337L,
                322L, 323L, 324L, 338L
        ));

        gasToPlaneRoute.addAll(to);
        gasToPlaneRoute.addAll(gates);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                                                                                                              && gasToPlaneRoute.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> gasToPlaneRoute.contains(graphPoint.getId());
    }
}
