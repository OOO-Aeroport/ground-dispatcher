package ru.deathkiller2009.ground.dispatcher.routes;

import org.springframework.data.relational.core.sql.In;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;
import ru.deathkiller2009.ground.dispatcher.logic.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FromPerronToGarage {

    private final static List<Long> toGarageRoute;


    static {

        List<Long> from = new java.util.ArrayList<>(Stream.iterate(80L, integer -> ++integer)
                .limit(17)
                .toList());

        List<Long> fromLower = Stream.iterate(48L, integer -> ++integer)
                .limit(17)
                .toList();

        from.addAll(fromLower);

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


        Stream<Long> up = Stream.iterate(301L, integer -> integer + 3)
                .limit(10);
        Stream<Long> up1 = Stream.iterate(302L, integer -> integer + 3)
                .limit(10);

        List<Long> upPath = Stream.concat(up, up1).toList();

        toGarageRoute = new ArrayList<>(
                List.of(17L, 18L, 19L, 20L,
                        24L, 25L, 26L, 27L,
                        29L, 30L, 31L, 32L)
        );
        toGarageRoute.addAll(gates);
        toGarageRoute.addAll(upPath);
        toGarageRoute.addAll(from);
    }

    public Predicate<GraphPoint> getRoute(long initialPoint, long endPoint) {
        return graphPoint -> (graphPoint.getStatus() == Status.EMPTY || graphPoint.getId() == initialPoint || graphPoint.getId() == endPoint)
                             && toGarageRoute.contains(graphPoint.getId());
    }

    public Predicate<GraphPoint> getRouteAnyway(long initialPoint, long endPoint) {
        return graphPoint -> toGarageRoute.contains(graphPoint.getId());
    }
}
