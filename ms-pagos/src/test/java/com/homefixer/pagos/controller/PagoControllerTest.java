package com.homefixer.pagos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homefixer.pagos.assembler.PagoModelAssembler;
import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import com.homefixer.pagos.model.MetodoPago;
import com.homefixer.pagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
class PagoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PagoService pagoService;
    
    @MockBean
    private PagoModelAssembler pagoAssembler;
    
    @Autowired
    private ObjectMapper objectMapper;
    
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
        pago.setReferenciaTransaccion("PAY-123456789");
        pago.setFechaCreacion(LocalDateTime.now());
    }
    
    @Test
    void testObtenerTodosLosPagos() throws Exception {
        // Arrange
        when(pagoService.obtenerTodosLosPagos()).thenReturn(Arrays.asList(pago));
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    void testObtenerPagoPorId() throws Exception {
        // Arrange
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(Optional.of(pago));
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testObtenerPagoPorIdNoEncontrado() throws Exception {
        // Arrange
        when(pagoService.obtenerPagoPorId(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCrearPago() throws Exception {
        // Arrange
        Pago nuevoPago = new Pago();
        nuevoPago.setIdSolicitud(1L);
        nuevoPago.setIdCliente(1L);
        nuevoPago.setIdTecnico(1L);
        nuevoPago.setMonto(new BigDecimal("50000"));
        nuevoPago.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        nuevoPago.setDescripcion("Pago por servicio");
        
        when(pagoService.crearPago(any(Pago.class))).thenReturn(pago);
        
        // Act & Assert
        mockMvc.perform(post("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoPago)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void testActualizarPago() throws Exception {
        // Arrange
        when(pagoService.actualizarPago(eq(1L), any(Pago.class))).thenReturn(pago);
        
        // Act & Assert
        mockMvc.perform(put("/api/pagos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk());
    }
    
    @Test
    void testProcesarPago() throws Exception {
        // Arrange
        when(pagoService.procesarPago(1L)).thenReturn(pago);
        
        // Act & Assert
        mockMvc.perform(put("/api/pagos/1/procesar"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testCompletarPago() throws Exception {
        // Arrange
        when(pagoService.completarPago(1L)).thenReturn(pago);
        
        // Act & Assert
        mockMvc.perform(put("/api/pagos/1/completar"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testObtenerPagosPorCliente() throws Exception {
        // Arrange
        when(pagoService.obtenerPagosPorCliente(1L)).thenReturn(Arrays.asList(pago));
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos/cliente/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testObtenerPagosPorTecnico() throws Exception {
        // Arrange
        when(pagoService.obtenerPagosPorTecnico(1L)).thenReturn(Arrays.asList(pago));
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos/tecnico/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testObtenerPagosPorEstado() throws Exception {
        // Arrange
        when(pagoService.obtenerPagosPorEstado(EstadoPago.PENDIENTE)).thenReturn(Arrays.asList(pago));
        
        // Act & Assert
        mockMvc.perform(get("/api/pagos/estado/PENDIENTE"))
                .andExpect(status().isOk());
    }
}