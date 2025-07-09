package com.homefixer.pagos.controller;

import com.homefixer.pagos.assembler.PagoModelAssembler;
import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import com.homefixer.pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "API para gestión de pagos")
public class PagoController {
    
    private final PagoService pagoService;
    private final PagoModelAssembler pagoAssembler;
    
    @GetMapping
    @Operation(summary = "Obtener todos los pagos")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    public CollectionModel<EntityModel<Pago>> obtenerTodosLosPagos() {
        List<EntityModel<Pago>> pagos = pagoService.obtenerTodosLosPagos()
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(pagos)
                .add(linkTo(methodOn(PagoController.class).obtenerTodosLosPagos()).withSelfRel());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID")
    @ApiResponse(responseCode = "200", description = "Pago encontrado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<EntityModel<Pago>> obtenerPagoPorId(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(pagoAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo pago")
    @ApiResponse(responseCode = "201", description = "Pago creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<EntityModel<Pago>> crearPago(@Valid @RequestBody Pago pago) {
        Pago nuevoPago = pagoService.crearPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoAssembler.toModel(nuevoPago));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago")
    @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<EntityModel<Pago>> actualizarPago(
            @PathVariable Long id, 
            @Valid @RequestBody Pago pago) {
        try {
            Pago pagoActualizado = pagoService.actualizarPago(id, pago);
            return ResponseEntity.ok(pagoAssembler.toModel(pagoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/procesar")
    @Operation(summary = "Procesar pago")
    @ApiResponse(responseCode = "200", description = "Pago procesado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<EntityModel<Pago>> procesarPago(@PathVariable Long id) {
        try {
            Pago pagoProcesado = pagoService.procesarPago(id);
            return ResponseEntity.ok(pagoAssembler.toModel(pagoProcesado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/completar")
    @Operation(summary = "Completar pago")
    @ApiResponse(responseCode = "200", description = "Pago completado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<EntityModel<Pago>> completarPago(@PathVariable Long id) {
        try {
            Pago pagoCompletado = pagoService.completarPago(id);
            return ResponseEntity.ok(pagoAssembler.toModel(pagoCompletado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener pagos por cliente")
    @ApiResponse(responseCode = "200", description = "Pagos del cliente obtenidos")
    public CollectionModel<EntityModel<Pago>> obtenerPagosPorCliente(@PathVariable Long idCliente) {
        List<EntityModel<Pago>> pagos = pagoService.obtenerPagosPorCliente(idCliente)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(pagos)
                .add(linkTo(methodOn(PagoController.class).obtenerPagosPorCliente(idCliente)).withSelfRel());
    }
    
    @GetMapping("/tecnico/{idTecnico}")
    @Operation(summary = "Obtener pagos por técnico")
    @ApiResponse(responseCode = "200", description = "Pagos del técnico obtenidos")
    public CollectionModel<EntityModel<Pago>> obtenerPagosPorTecnico(@PathVariable Long idTecnico) {
        List<EntityModel<Pago>> pagos = pagoService.obtenerPagosPorTecnico(idTecnico)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(pagos)
                .add(linkTo(methodOn(PagoController.class).obtenerPagosPorTecnico(idTecnico)).withSelfRel());
    }
    
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pagos por estado")
    @ApiResponse(responseCode = "200", description = "Pagos filtrados por estado")
    public CollectionModel<EntityModel<Pago>> obtenerPagosPorEstado(@PathVariable EstadoPago estado) {
        List<EntityModel<Pago>> pagos = pagoService.obtenerPagosPorEstado(estado)
                .stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(pagos)
                .add(linkTo(methodOn(PagoController.class).obtenerPagosPorEstado(estado)).withSelfRel());
    }
}