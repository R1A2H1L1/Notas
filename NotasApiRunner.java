package com.notas.app.karate;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotasApiRunner {

    @LocalServerPort
    private int port;

    @Karate.Test
    Karate testNotas() {
        return Karate.run("classpath:karate/notas/notas.feature")
                     .systemProperty("server.port", String.valueOf(port));
    }
}
