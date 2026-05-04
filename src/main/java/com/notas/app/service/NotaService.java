package com.notas.app.service;

import com.notas.app.entity.Nota;
import com.notas.app.repository.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public Nota guardarNota(Nota nota) {
        return notaRepository.save(nota);
    }

    public List<Nota> obtenerTodasLasNotas() {
        return notaRepository.findAll();
    }

    public Optional<Nota> obtenerNotaPorId(Long id) {
        return notaRepository.findById(id);
    }

    public void eliminarNota(Long id) {
        notaRepository.deleteById(id);
    }
}
