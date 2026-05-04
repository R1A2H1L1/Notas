package com.notas.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notas.app.entity.Nota;
import com.notas.app.service.NotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotaController.class)
@DisplayName("Pruebas unitarias - NotaController")
class NotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotaService notaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Nota notaEjemplo;

    @BeforeEach
    void setUp() {
        notaEjemplo = new Nota();
        notaEjemplo.setId(1L);
        notaEjemplo.setTitulo("Título de prueba");
        notaEjemplo.setContenido("Contenido de prueba");
        notaEjemplo.setFechaCreacion(LocalDateTime.of(2024, 1, 15, 10, 30));
    }

    // ─────────────────────────────────────────────────────
    //  POST /notas
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /notas - debe crear una nota y retornar 201")
    void crearNota_debeRetornar201ConNotaCreada() throws Exception {
        Nota notaNueva = new Nota("Título de prueba", "Contenido de prueba");
        when(notaService.guardarNota(any(Nota.class))).thenReturn(notaEjemplo);

        mockMvc.perform(post("/notas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notaNueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título de prueba"))
                .andExpect(jsonPath("$.contenido").value("Contenido de prueba"));

        verify(notaService, times(1)).guardarNota(any(Nota.class));
    }

    // ─────────────────────────────────────────────────────
    //  GET /notas
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /notas - debe retornar todas las notas con 200")
    void obtenerTodasLasNotas_debeRetornar200ConListaDeNotas() throws Exception {
        Nota segundaNota = new Nota();
        segundaNota.setId(2L);
        segundaNota.setTitulo("Segunda nota");
        segundaNota.setContenido("Segundo contenido");
        segundaNota.setFechaCreacion(LocalDateTime.of(2024, 2, 1, 9, 0));

        List<Nota> notas = Arrays.asList(notaEjemplo, segundaNota);
        when(notaService.obtenerTodasLasNotas()).thenReturn(notas);

        mockMvc.perform(get("/notas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").value("Título de prueba"))
                .andExpect(jsonPath("$[1].titulo").value("Segunda nota"));

        verify(notaService, times(1)).obtenerTodasLasNotas();
    }

    // ─────────────────────────────────────────────────────
    //  GET /notas/{id}
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /notas/{id} - debe retornar la nota con 200 cuando existe")
    void obtenerNotaPorId_debeRetornar200CuandoExiste() throws Exception {
        when(notaService.obtenerNotaPorId(1L)).thenReturn(Optional.of(notaEjemplo));

        mockMvc.perform(get("/notas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título de prueba"));

        verify(notaService, times(1)).obtenerNotaPorId(1L);
    }

    @Test
    @DisplayName("GET /notas/{id} - debe retornar 404 cuando la nota no existe")
    void obtenerNotaPorId_debeRetornar404CuandoNoExiste() throws Exception {
        when(notaService.obtenerNotaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/notas/99"))
                .andExpect(status().isNotFound());

        verify(notaService, times(1)).obtenerNotaPorId(99L);
    }

    // ─────────────────────────────────────────────────────
    //  DELETE /notas/{id}
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /notas/{id} - debe eliminar la nota y retornar 204 cuando existe")
    void eliminarNota_debeRetornar204CuandoExiste() throws Exception {
        when(notaService.obtenerNotaPorId(1L)).thenReturn(Optional.of(notaEjemplo));
        doNothing().when(notaService).eliminarNota(1L);

        mockMvc.perform(delete("/notas/1"))
                .andExpect(status().isNoContent());

        verify(notaService, times(1)).obtenerNotaPorId(1L);
        verify(notaService, times(1)).eliminarNota(1L);
    }

    @Test
    @DisplayName("DELETE /notas/{id} - debe retornar 404 cuando la nota no existe")
    void eliminarNota_debeRetornar404CuandoNoExiste() throws Exception {
        when(notaService.obtenerNotaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/notas/99"))
                .andExpect(status().isNotFound());

        verify(notaService, times(1)).obtenerNotaPorId(99L);
        verify(notaService, never()).eliminarNota(any());
    }
}
