package com.homefixer.repository;

import com.homefixer.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    // Valoraciones de un técnico
    List<Valoracion> findByIdTecnico(Long idTecnico);

    // Valoraciones hechas por un cliente
    List<Valoracion> findByIdCliente(Long idCliente);

    // Promedio de calificación de un técnico
    // (Se implementa con @Query en el servicio si se necesita un promedio directo)
}