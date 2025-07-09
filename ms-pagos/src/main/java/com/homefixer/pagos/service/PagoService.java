package com.homefixer.pagos.service;

import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import com.homefixer.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoService {
    
    private final PagoRepository pagoRepository;
    
    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }
    
    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }
    
    @Transactional
    public Pago crearPago(Pago pago) {
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setReferenciaTransaccion(generarReferencia());
        return pagoRepository.save(pago);
    }
    
    @Transactional
    public Pago actualizarPago(Long id, Pago pagoActualizado) {
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setMonto(pagoActualizado.getMonto());
                    pago.setMetodoPago(pagoActualizado.getMetodoPago());
                    pago.setDescripcion(pagoActualizado.getDescripcion());
                    return pagoRepository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    @Transactional
    public Pago procesarPago(Long id) {
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setEstadoPago(EstadoPago.PROCESANDO);
                    pago.setFechaProcesamiento(LocalDateTime.now());
                    return pagoRepository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    @Transactional
    public Pago completarPago(Long id) {
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setEstadoPago(EstadoPago.COMPLETADO);
                    pago.setFechaProcesamiento(LocalDateTime.now());
                    return pagoRepository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    public List<Pago> obtenerPagosPorCliente(Long idCliente) {
        return pagoRepository.findByIdCliente(idCliente);
    }
    
    public List<Pago> obtenerPagosPorTecnico(Long idTecnico) {
        return pagoRepository.findByIdTecnico(idTecnico);
    }
    
    public List<Pago> obtenerPagosPorEstado(EstadoPago estado) {
        return pagoRepository.findByEstadoPago(estado);
    }
    
    // Generar referencia única para el pago
    private String generarReferencia() {
        return "PAY-" + System.currentTimeMillis();
    }
}
