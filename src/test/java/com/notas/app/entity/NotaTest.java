package com.notas.app.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias - Nota Entity")
class NotaTest {

    @Test
    @DisplayName("Constructor por defecto debe crear un objeto Nota con campos nulos")
    void constructorDefecto_debeCrearNotaConCamposNulos() {
        Nota nota = new Nota();

        assertNotNull(nota);
        assertNull(nota.getId());
        assertNull(nota.getTitulo());
        assertNull(nota.getContenido());
        assertNull(nota.getFechaCreacion());
    }

    @Test
    @DisplayName("Constructor con argumentos debe inicializar titulo y contenido")
    void constructorConArgumentos_debeInicializarTituloYContenido() {
        Nota nota = new Nota("Mi título", "Mi contenido");

        assertEquals("Mi título", nota.getTitulo());
        assertEquals("Mi contenido", nota.getContenido());
        assertNull(nota.getId());
        assertNull(nota.getFechaCreacion());
    }

    @Test
    @DisplayName("prePersist debe asignar fechaCreacion automáticamente")
    void prePersist_debeAsignarFechaCreacion() {
        Nota nota = new Nota();
        assertNull(nota.getFechaCreacion());

        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        nota.prePersist();
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        assertNotNull(nota.getFechaCreacion());
        assertTrue(nota.getFechaCreacion().isAfter(antes));
        assertTrue(nota.getFechaCreacion().isBefore(despues));
    }

    @Test
    @DisplayName("Setters y getters deben funcionar correctamente")
    void settersGetters_debenFuncionarCorrectamente() {
        Nota nota = new Nota();
        LocalDateTime fecha = LocalDateTime.of(2024, 6, 15, 12, 0);

        nota.setId(42L);
        nota.setTitulo("Título actualizado");
        nota.setContenido("Contenido actualizado");
        nota.setFechaCreacion(fecha);

        assertEquals(42L, nota.getId());
        assertEquals("Título actualizado", nota.getTitulo());
        assertEquals("Contenido actualizado", nota.getContenido());
        assertEquals(fecha, nota.getFechaCreacion());
    }

}
