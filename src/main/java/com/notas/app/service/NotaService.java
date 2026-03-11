package com.notas.app.service;

import com.notas.app.entity.Nota;
import com.notas.app.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    @Autowired
    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    /**
     * Guarda una nueva nota en la base de datos.
     *
     * @param nota la nota a guardar
     * @return la nota guardada con su id asignado
     */
    public Nota guardarNota(Nota nota) {
        return notaRepository.save(nota);
    }

    /**
     * Obtiene todas las notas almacenadas.
     *
     * @return lista de todas las notas
     */
    public List<Nota> obtenerTodasLasNotas() {
        return notaRepository.findAll();
    }

    /**
     * Obtiene una nota por su id.
     *
     * @param id el identificador de la nota
     * @return un Optional con la nota si existe, vacío si no
     */
    public Optional<Nota> obtenerNotaPorId(Long id) {
        return notaRepository.findById(id);
    }

    /**
     * Elimina una nota por su id.
     *
     * @param id el identificador de la nota a eliminar
     */
    public void eliminarNota(Long id) {
        notaRepository.deleteById(id);
    }
}
