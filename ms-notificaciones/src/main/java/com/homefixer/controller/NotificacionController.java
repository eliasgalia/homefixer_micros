package com.homefixer.controller;

import com.homefixer.model.Notificacion;
import com.homefixer.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "API para gestión de notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService servicio;

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones activas")
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> obtenerTodas() {
        List<EntityModel<Notificacion>> lista = servicio.obtenerTodas().stream()
                .map(n -> EntityModel.of(n,
                        linkTo(methodOn(NotificacionController.class).obtenerPorId(n.getIdNotificacion())).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(NotificacionController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID")
    public ResponseEntity<EntityModel<Notificacion>> obtenerPorId(@PathVariable Long id) {
        return servicio.obtenerPorId(id)
                .map(n -> EntityModel.of(n,
                        linkTo(methodOn(NotificacionController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(NotificacionController.class).obtenerTodas()).withRel("notificaciones")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destinatario/{id}")
    @Operation(summary = "Obtener notificaciones de un destinatario")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorDestinatario(id));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Filtrar por tipo de notificación")
    public ResponseEntity<List<Notificacion>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(servicio.obtenerPorTipo(tipo));
    }

    @GetMapping("/no-leidas/{id}")
    @Operation(summary = "Obtener notificaciones no leídas de un usuario")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidas(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerNoLeidasPorUsuario(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva notificación")
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody Notificacion noti) {
        return ResponseEntity.ok(servicio.crear(noti));
    }

    @PutMapping("/{id}/marcar-leida")
    @Operation(summary = "Marcar una notificación como leída")
    public ResponseEntity<Notificacion> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificación (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}