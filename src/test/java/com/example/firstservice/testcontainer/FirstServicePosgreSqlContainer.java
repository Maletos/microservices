package com.example.firstservice.testcontainer;

import org.testcontainers.containers.PostgreSQLContainer;

public class FirstServicePosgreSqlContainer extends PostgreSQLContainer<FirstServicePosgreSqlContainer> {
    private static final String IMAGE_VERSION = "postgres:14";
    private static FirstServicePosgreSqlContainer container;

    private FirstServicePosgreSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static FirstServicePosgreSqlContainer getInstance() {
        if (container == null) {
            container = new FirstServicePosgreSqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
