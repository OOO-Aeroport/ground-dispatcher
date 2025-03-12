package ru.deathkiller2009.ground.dispatcher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GroundDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroundDispatcherApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(MapService mapService) {
        return _ -> mapService.initMap();
    }

}
