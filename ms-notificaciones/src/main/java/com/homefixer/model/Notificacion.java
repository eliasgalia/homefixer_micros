package com.homefixer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    @Column(nullable = false)
    private String tipoNotificacion; // Ej: SOLICITUD, ASIGNACION, COMPLETADO...

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false)
    private Long destinatarioId; // ID del usuario que recibe la notificación

    @NotBlank(message = "El tipo de destinatario es obligatorio")
    @Column(nullable = false)
    private String tipoDestinatario; // CLIENTE o TECNICO

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leida = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activa = true;
}