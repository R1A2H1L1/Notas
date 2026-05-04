package com.notas.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notas.app.entity.Nota;
import com.notas.app.service.NotaService;

@RestController
@RequestMapping("/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaService notaService;

    @Autowired
    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    /**
     * POST /notas → Crear una nueva nota
     */
    @PostMapping
    public ResponseEntity<Nota> crearNota(@RequestBody Nota nota) {
        Nota notaGuardada = notaService.guardarNota(nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(notaGuardada);
    }

    /**
     * GET /notas → Obtener todas las notas
     */
    @GetMapping
    public ResponseEntity<List<Nota>> obtenerTodasLasNotas() {
        List<Nota> notas = notaService.obtenerTodasLasNotas();
        return ResponseEntity.ok(notas);
    }

    /**
     * GET /notas/{id} → Obtener una nota por id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtenerNotaPorId(@PathVariable Long id) {
        Optional<Nota> nota = notaService.obtenerNotaPorId(id);
        return nota.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /notas/{id} → Eliminar una nota
     */
    @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {
      return notaService.obtenerNotaPorId(id)
              .<ResponseEntity<Void>>map(nota -> {
                  notaService.eliminarNota(id);
                  return ResponseEntity.noContent().build();
              })
              .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
