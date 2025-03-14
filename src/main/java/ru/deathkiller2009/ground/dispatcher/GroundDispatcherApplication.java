package ru.deathkiller2009.ground.dispatcher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import ru.deathkiller2009.ground.dispatcher.logic.AirportMap;
import ru.deathkiller2009.ground.dispatcher.logic.GraphPoint;

import java.util.List;

@SpringBootApplication
public class GroundDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroundDispatcherApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

}
