package com.homefixer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "valoraciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValoracion;

    @Column(nullable = false)
    private Long idSolicitud;

    @Column(nullable = false)
    private Long idCliente;

    @Column(nullable = false)
    private Long idTecnico;

    @NotNull
    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer calificacion;

    @Size(max = 500)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fechaValoracion;

    @Min(1) @Max(5)
    private Integer calificacionPuntualidad;

    @Min(1) @Max(5)
    private Integer calificacionCalidad;

    @Min(1) @Max(5)
    private Integer calificacionComunicacion;

    private Boolean recomendaria;
}