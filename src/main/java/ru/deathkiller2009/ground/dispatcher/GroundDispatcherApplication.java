package ru.deathkiller2009.ground.dispatcher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.deathkiller2009.ground.dispatcher.logic.AirportMap;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;

import java.util.List;

@SpringBootApplication
public class GroundDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroundDispatcherApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AirportMap airportMap) {
        return args -> {
            List<GraphPoint> graphPoints = airportMap.buildRouteForGas(297);
            for (int i = 0; i < graphPoints.size(); i++) {
                airportMap.checkIfCarCanGo(graphPoints.get(i).getId(), graphPoints.get(i + 1).getId());
            }
        };
    }

}
