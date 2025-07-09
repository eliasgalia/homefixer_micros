package com.homefixer.service;

import com.homefixer.model.Notificacion;
import com.homefixer.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repo;

    @InjectMocks
    private NotificacionService servicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearNotificacion() {
        Notificacion noti = Notificacion.builder()
                .tipoNotificacion("ASIGNACION")
                .titulo("Asignado")
                .mensaje("Fuiste asignado")
                .destinatarioId(1L)
                .tipoDestinatario("TECNICO")
                .fechaEnvio(LocalDateTime.now())
                .build();

        when(repo.save(any(Notificacion.class))).thenReturn(noti);

        Notificacion resultado = servicio.crear(noti);
        assertEquals("ASIGNACION", resultado.getTipoNotificacion());
        verify(repo, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testObtenerTodas() {
        when(repo.findByActivaTrue()).thenReturn(List.of(new Notificacion(), new Notificacion()));
        assertEquals(2, servicio.obtenerTodas().size());
    }

    @Test
    void testMarcarComoLeida() {
        Notificacion n = new Notificacion();
        n.setIdNotificacion(1L);
        n.setLeida(false);
        when(repo.findById(1L)).thenReturn(Optional.of(n));
        when(repo.save(any(Notificacion.class))).thenReturn(n);

        Notificacion actualizada = servicio.marcarComoLeida(1L);
        assertTrue(actualizada.getLeida());
    }

    @Test
    void testEliminar() {
        Notificacion n = new Notificacion();
        n.setIdNotificacion(5L);
        n.setActiva(true);
        when(repo.findById(5L)).thenReturn(Optional.of(n));

        servicio.eliminar(5L);
        assertFalse(n.getActiva());
        verify(repo).save(n);
    }

    @Test
    void testContarNoLeidas() {
        when(repo.countByDestinatarioIdAndLeidaAndActivaTrue(1L, false)).thenReturn(3L);
        assertEquals(3, servicio.contarNoLeidasPorUsuario(1L));
    }

    @Test
    void testObtenerNoLeidas() {
        List<Notificacion> lista = List.of(new Notificacion(), new Notificacion());
        when(repo.findByDestinatarioIdAndLeidaAndActivaTrue(1L, false)).thenReturn(lista);
        assertEquals(2, servicio.obtenerNoLeidasPorUsuario(1L).size());
    }
}