package com.homefixer.service;

import com.homefixer.model.Notificacion;
import com.homefixer.repository.NotificacionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionRepository repo;

    public List<Notificacion> obtenerTodas() {
        log.info("📩 Obteniendo todas las notificaciones activas");
        return repo.findByActivaTrue();
    }

    public Optional<Notificacion> obtenerPorId(Long id) {
        return repo.findById(id).filter(Notificacion::getActiva);
    }

    public List<Notificacion> obtenerPorDestinatario(Long id) {
        return repo.findByDestinatarioIdAndActivaTrue(id);
    }

    public List<Notificacion> obtenerPorTipo(String tipo) {
        return repo.findByTipoNotificacionAndActivaTrue(tipo.toUpperCase());
    }

    public List<Notificacion> obtenerNoLeidasPorUsuario(Long id) {
        return repo.findByDestinatarioIdAndLeidaAndActivaTrue(id, false);
    }

    public long contarNoLeidasPorUsuario(Long id) {
        return repo.countByDestinatarioIdAndLeidaAndActivaTrue(id, false);
    }

    public Notificacion crear(@Valid Notificacion noti) {
        noti.setFechaEnvio(LocalDateTime.now());
        noti.setLeida(false);
        noti.setActiva(true);
        log.info("✅ Notificación creada para usuario {}", noti.getDestinatarioId());
        return repo.save(noti);
    }

    public Notificacion marcarComoLeida(Long id) {
        return repo.findById(id)
                .map(noti -> {
                    noti.setLeida(true);
                    log.info("📬 Notificación {} marcada como leída", id);
                    return repo.save(noti);
                }).orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(noti -> {
            noti.setActiva(false);
            repo.save(noti);
            log.info("🗑 Notificación {} eliminada (soft delete)", id);
        });
    }
}