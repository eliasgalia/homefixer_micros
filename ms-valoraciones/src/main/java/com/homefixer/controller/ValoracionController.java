package com.homefixer.controller;

import com.homefixer.model.Valoracion;
import com.homefixer.service.ValoracionService;
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
@RequestMapping("/api/valoraciones")
@Tag(name = "Valoraciones", description = "API para gestión de valoraciones")
@RequiredArgsConstructor
public class ValoracionController {

    private final ValoracionService service;

    @PostMapping
    @Operation(summary = "Crear nueva valoración")
    public ResponseEntity<Valoracion> crear(@Valid @RequestBody Valoracion v) {
        return ResponseEntity.ok(service.crear(v));
    }

    @GetMapping("/tecnico/{id}")
    @Operation(summary = "Listar valoraciones de un técnico")
    public ResponseEntity<CollectionModel<EntityModel<Valoracion>>> listarPorTecnico(@PathVariable Long id) {
        List<EntityModel<Valoracion>> lista = service.listarPorTecnico(id).stream()
            .map(val -> EntityModel.of(val,
                linkTo(methodOn(ValoracionController.class).listarPorTecnico(id)).withSelfRel(),
                linkTo(methodOn(ValoracionController.class).obtenerPorId(val.getIdValoracion())).withRel("detalle")))
            .toList();

        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ValoracionController.class).listarPorTecnico(id)).withSelfRel()));
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Listar valoraciones hechas por un cliente")
    public ResponseEntity<List<Valoracion>> listarPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPorCliente(id));
    }

    @GetMapping("/promedio/{id}")
    @Operation(summary = "Obtener promedio de valoración de un técnico")
    public ResponseEntity<Double> promedio(@PathVariable Long id) {
        double avg = service.promedioPorTecnico(id);
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener valoración por ID")
    public ResponseEntity<EntityModel<Valoracion>> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
            .map(val -> EntityModel.of(val,
                linkTo(methodOn(ValoracionController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(ValoracionController.class).listarPorTecnico(val.getIdTecnico())).withRel("valoraciones-tecnico")))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}