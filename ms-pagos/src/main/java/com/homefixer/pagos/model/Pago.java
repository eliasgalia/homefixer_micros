package com.homefixer.pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;
    
    @NotNull(message = "ID de solicitud es obligatorio")
    @Column(nullable = false)
    private Long idSolicitud;
    
    @NotNull(message = "ID de cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;
    
    @NotNull(message = "ID de técnico es obligatorio")
    @Column(nullable = false)
    private Long idTecnico;
    
    @NotNull(message = "Monto es obligatorio")
    @DecimalMin(value = "0.01", message = "Monto debe ser mayor a 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;
    
    @NotNull(message = "Método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    @NotNull(message = "Estado de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaProcesamiento;
    
    @Size(max = 500, message = "Descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @Size(max = 100, message = "Referencia no puede exceder 100 caracteres")
    private String referenciaTransaccion;
    
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estadoPago == null) {
            this.estadoPago = EstadoPago.PENDIENTE;
        }
    }
}