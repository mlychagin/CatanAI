package com.SpringField.server;

import com.SpringField.engine.BoardState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BoardStateRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new BoardStateEntity((byte) 4)));
            log.info("Preloading " + repository.save(new BoardStateEntity((byte) 2)));
        };
    }
}