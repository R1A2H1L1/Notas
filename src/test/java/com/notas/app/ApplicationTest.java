package com.notas.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Prueba de contexto - Application")
class ApplicationTest {

    @Test
    @DisplayName("El contexto de Spring Boot debe cargarse correctamente")
    void contextLoads() {
    }
}
