package com.homefixer.service;

import com.homefixer.model.Valoracion;
import com.homefixer.repository.ValoracionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValoracionService {

    private final ValoracionRepository repo;

    /** Crea una nueva valoración */
    public Valoracion crear(@Valid Valoracion v) {
        v.setFechaValoracion(LocalDateTime.now());
        Valoracion guardada = repo.save(v);
        log.info("✅ Valoración creada: {}", guardada.getIdValoracion());
        return guardada;
    }

    /** Obtiene todas las valoraciones de un técnico */
    public List<Valoracion> listarPorTecnico(Long idTecnico) {
        return repo.findByIdTecnico(idTecnico);
    }

    /** Obtiene todas las valoraciones realizadas por un cliente */
    public List<Valoracion> listarPorCliente(Long idCliente) {
        return repo.findByIdCliente(idCliente);
    }

    /** Calcula el promedio de calificaciones de un técnico */
    public double promedioPorTecnico(Long idTecnico) {
        List<Valoracion> lista = repo.findByIdTecnico(idTecnico);
        if (lista.isEmpty()) {
            return 0.0;
        }
        DoubleSummaryStatistics stats = lista.stream()
            .mapToDouble(Valoracion::getCalificacion)
            .summaryStatistics();
        return stats.getAverage();
    }

    /** Obtiene una valoración por su ID */
    public Optional<Valoracion> obtenerPorId(Long id) {
        return repo.findById(id);
    }
}