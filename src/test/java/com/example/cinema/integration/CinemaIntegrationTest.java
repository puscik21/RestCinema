package com.example.cinema.integration;

import com.example.cinema.CinemaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = "classpath:test/testData.sql")
@SpringBootTest(classes = CinemaApplication.class)
public class CinemaIntegrationTest {

    @Test
    void setUp() {
        assertTrue(true);
    }
}
