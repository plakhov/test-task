package com.home.test.base;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    protected static final Faker FAKER = Faker.instance();
    private static final int POSTGRES_PORT = 5432;
    private static final int TMPFS_SIZE = 500 * 1024 * 1024;


    protected MockMvc mockMvc;

    private static final String IMAGE_VERSION = "postgres";
    private static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(IMAGE_VERSION)
            .withDatabaseName("test")
            .withUsername("admin")
            .withPassword("admin")
            .withExposedPorts(POSTGRES_PORT)
            .withTmpFs(Collections.singletonMap("/var/lib/postgresql", "size=" + TMPFS_SIZE));

    static {
        postgreSQLContainer.start();
        System.setProperty("DB_PORT", postgreSQLContainer.getMappedPort(POSTGRES_PORT).toString());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
