package com.homefixer.pagos.service;

import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import com.homefixer.pagos.model.MetodoPago;
import com.homefixer.pagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {
    
    @Mock
    private PagoRepository pagoRepository;
    
    @InjectMocks
    private PagoService pagoService;
    
    private Pago pago;
    
    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdSolicitud(1L);
        pago.setIdCliente(1L);
        pago.setIdTecnico(1L);
        pago.setMonto(new BigDecimal("50000"));
        pago.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setDescripcion("Pago por servicio");
        pago.setFechaCreacion(LocalDateTime.now());
    }
    
    @Test
    void testObtenerTodosLosPagos() {
        // Arrange
        List<Pago> pagos = Arrays.asList(pago);
        when(pagoRepository.findAll()).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // Assert
        assertEquals(1, resultado.size());
        verify(pagoRepository).findAll();
    }
    
    @Test
    void testObtenerPagoPorId() {
        // Arrange
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        
        // Act
        Optional<Pago> resultado = pagoService.obtenerPagoPorId(1L);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pago.getIdPago(), resultado.get().getIdPago());
        verify(pagoRepository).findById(1L);
    }
    
    @Test
    void testCrearPago() {
        // Arrange
        Pago nuevoPago = new Pago();
        nuevoPago.setIdSolicitud(1L);
        nuevoPago.setIdCliente(1L);
        nuevoPago.setIdTecnico(1L);
        nuevoPago.setMonto(new BigDecimal("50000"));
        nuevoPago.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        
        // Act
        Pago resultado = pagoService.crearPago(nuevoPago);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoPago.PENDIENTE, resultado.getEstadoPago());
        assertNotNull(resultado.getReferenciaTransaccion());
        verify(pagoRepository).save(any(Pago.class));
    }
    
    @Test
    void testActualizarPago() {
        // Arrange
        Pago pagoActualizado = new Pago();
        pagoActualizado.setMonto(new BigDecimal("75000"));
        pagoActualizado.setMetodoPago(MetodoPago.TRANSFERENCIA);
        pagoActualizado.setDescripcion("Descripción actualizada");
        
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        
        // Act
        Pago resultado = pagoService.actualizarPago(1L, pagoActualizado);
        
        // Assert
        assertNotNull(resultado);
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
    }
    
    @Test
    void testProcesarPago() {
        // Arrange
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        
        // Act
        Pago resultado = pagoService.procesarPago(1L);
        
        // Assert
        assertNotNull(resultado);
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
    }
    
    @Test
    void testCompletarPago() {
        // Arrange
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        
        // Act
        Pago resultado = pagoService.completarPago(1L);
        
        // Assert
        assertNotNull(resultado);
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
    }
    
    @Test
    void testObtenerPagosPorCliente() {
        // Arrange
        List<Pago> pagos = Arrays.asList(pago);
        when(pagoRepository.findByIdCliente(1L)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPagosPorCliente(1L);
        
        // Assert
        assertEquals(1, resultado.size());
        verify(pagoRepository).findByIdCliente(1L);
    }
    
    @Test
    void testObtenerPagosPorTecnico() {
        // Arrange
        List<Pago> pagos = Arrays.asList(pago);
        when(pagoRepository.findByIdTecnico(1L)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPagosPorTecnico(1L);
        
        // Assert
        assertEquals(1, resultado.size());
        verify(pagoRepository).findByIdTecnico(1L);
    }
    
    @Test
    void testObtenerPagosPorEstado() {
        // Arrange
        List<Pago> pagos = Arrays.asList(pago);
        when(pagoRepository.findByEstadoPago(EstadoPago.PENDIENTE)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPagosPorEstado(EstadoPago.PENDIENTE);
        
        // Assert
        assertEquals(1, resultado.size());
        verify(pagoRepository).findByEstadoPago(EstadoPago.PENDIENTE);
    }
    
    @Test
    void testPagoNoEncontrado() {
        // Arrange
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pagoService.actualizarPago(999L, pago);
        });
    }
}