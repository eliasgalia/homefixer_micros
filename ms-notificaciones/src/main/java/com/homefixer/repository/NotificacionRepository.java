package com.homefixer.repository;

import com.homefixer.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByDestinatarioIdAndActivaTrue(Long destinatarioId);

    List<Notificacion> findByTipoNotificacionAndActivaTrue(String tipoNotificacion);

    List<Notificacion> findByDestinatarioIdAndLeidaAndActivaTrue(Long destinatarioId, Boolean leida);

    List<Notificacion> findByActivaTrue();

    long countByDestinatarioIdAndLeidaAndActivaTrue(Long destinatarioId, Boolean leida);
}