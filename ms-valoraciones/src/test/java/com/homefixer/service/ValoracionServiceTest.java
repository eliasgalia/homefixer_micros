package com.homefixer.service;

import com.homefixer.model.Valoracion;
import com.homefixer.repository.ValoracionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValoracionServiceTest {

    @Mock
    private ValoracionRepository repo;

    @InjectMocks
    private ValoracionService service;

    @Test
    void testCrearValoracion() {
        Valoracion v = Valoracion.builder()
            .idSolicitud(1L).idCliente(1L).idTecnico(1L)
            .calificacion(4).comentario("Muy bien").build();
        when(repo.save(any())).thenReturn(v);

        Valoracion result = service.crear(v);
        assertThat(result.getCalificacion()).isEqualTo(4);
        verify(repo).save(v);
    }

    @Test
    void testListarPorTecnico() {
        when(repo.findByIdTecnico(2L)).thenReturn(List.of(new Valoracion(), new Valoracion()));
        assertThat(service.listarPorTecnico(2L)).hasSize(2);
    }

    @Test
    void testListarPorCliente() {
        when(repo.findByIdCliente(3L)).thenReturn(List.of(new Valoracion()));
        assertThat(service.listarPorCliente(3L)).hasSize(1);
    }

    @Test
    void testPromedioPorTecnico() {
        Valoracion v1 = Valoracion.builder().calificacion(3).build();
        Valoracion v2 = Valoracion.builder().calificacion(5).build();
        when(repo.findByIdTecnico(4L)).thenReturn(List.of(v1, v2));
        assertThat(service.promedioPorTecnico(4L)).isEqualTo(4.0);
    }

    @Test
    void testObtenerPorId() {
        Valoracion v = new Valoracion();
        when(repo.findById(5L)).thenReturn(Optional.of(v));
        assertThat(service.obtenerPorId(5L)).isPresent();
    }
}