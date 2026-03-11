package com.notas.app.service;

import com.notas.app.entity.Nota;
import com.notas.app.repository.NotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - NotaService")
class NotaServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private NotaService notaService;

    private Nota notaEjemplo;

    @BeforeEach
    void setUp() {
        notaEjemplo = new Nota();
        notaEjemplo.setId(1L);
        notaEjemplo.setTitulo("Nota de prueba");
        notaEjemplo.setContenido("Contenido de la nota de prueba");
        notaEjemplo.setFechaCreacion(LocalDateTime.now());
    }

    // ─────────────────────────────────────────────────────
    //  guardarNota
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar una nota correctamente")
    void guardarNota_debeRetornarNotaGuardada() {
        // Arrange
        Nota notaNueva = new Nota("Título nuevo", "Contenido nuevo");
        when(notaRepository.save(any(Nota.class))).thenReturn(notaEjemplo);

        // Act
        Nota resultado = notaService.guardarNota(notaNueva);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nota de prueba", resultado.getTitulo());
        verify(notaRepository, times(1)).save(notaNueva);
    }

    @Test
    @DisplayName("Debe llamar al repositorio al guardar una nota")
    void guardarNota_debeLlamarAlRepositorio() {
        // Arrange
        when(notaRepository.save(any(Nota.class))).thenReturn(notaEjemplo);

        // Act
        notaService.guardarNota(notaEjemplo);

        // Assert
        verify(notaRepository, times(1)).save(notaEjemplo);
    }

    // ─────────────────────────────────────────────────────
    //  obtenerTodasLasNotas
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar todas las notas guardadas")
    void obtenerTodasLasNotas_debeRetornarListaCompleta() {
        // Arrange
        Nota segundaNota = new Nota();
        segundaNota.setId(2L);
        segundaNota.setTitulo("Segunda nota");
        segundaNota.setContenido("Segundo contenido");
        segundaNota.setFechaCreacion(LocalDateTime.now());

        List<Nota> notasEsperadas = Arrays.asList(notaEjemplo, segundaNota);
        when(notaRepository.findAll()).thenReturn(notasEsperadas);

        // Act
        List<Nota> resultado = notaService.obtenerTodasLasNotas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Nota de prueba", resultado.get(0).getTitulo());
        assertEquals("Segunda nota", resultado.get(1).getTitulo());
        verify(notaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay notas")
    void obtenerTodasLasNotas_listaVaciaSiNoHayNotas() {
        // Arrange
        when(notaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Nota> resultado = notaService.obtenerTodasLasNotas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notaRepository, times(1)).findAll();
    }

    // ─────────────────────────────────────────────────────
    //  obtenerNotaPorId
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar la nota cuando existe el id")
    void obtenerNotaPorId_debeRetornarNotaCuandoExiste() {
        // Arrange
        when(notaRepository.findById(1L)).thenReturn(Optional.of(notaEjemplo));

        // Act
        Optional<Nota> resultado = notaService.obtenerNotaPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Nota de prueba", resultado.get().getTitulo());
        verify(notaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el id no existe")
    void obtenerNotaPorId_debeRetornarOptionalVacioCuandoNoExiste() {
        // Arrange
        when(notaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Nota> resultado = notaService.obtenerNotaPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(notaRepository, times(1)).findById(99L);
    }

    // ─────────────────────────────────────────────────────
    //  eliminarNota
    // ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar la nota correctamente")
    void eliminarNota_debeEliminarCorrectamente() {
        // Arrange
        doNothing().when(notaRepository).deleteById(1L);

        // Act
        notaService.eliminarNota(1L);

        // Assert
        verify(notaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe llamar deleteById con el id correcto")
    void eliminarNota_debeLlamarDeleteByIdConIdCorrecto() {
        // Arrange
        Long idAEliminar = 5L;
        doNothing().when(notaRepository).deleteById(idAEliminar);

        // Act
        notaService.eliminarNota(idAEliminar);

        // Assert
        verify(notaRepository, times(1)).deleteById(idAEliminar);
        verify(notaRepository, never()).deleteById(1L);
    }
}
