package com.notas.app.controller;

import com.notas.app.dto.NotaRequest;
import com.notas.app.entity.Nota;
import com.notas.app.service.NotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @PostMapping
    public ResponseEntity<Nota> crearNota(@RequestBody NotaRequest notaRequest) {
        Nota nota = new Nota(notaRequest.titulo(), notaRequest.contenido());
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.guardarNota(nota));
    }

    @GetMapping
    public ResponseEntity<List<Nota>> obtenerTodasLasNotas() {
        return ResponseEntity.ok(notaService.obtenerTodasLasNotas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtenerNotaPorId(@PathVariable Long id) {
        return notaService.obtenerNotaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
