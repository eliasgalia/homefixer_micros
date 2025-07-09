package com.homefixer.config;

import com.github.javafaker.Faker;
import com.homefixer.model.Notificacion;
import com.homefixer.repository.NotificacionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final NotificacionRepository repo;
    private final Faker faker = new Faker(Locale.forLanguageTag("es"));    private final String[] tipos = {"SOLICITUD", "ASIGNACION", "COMPLETADO", "CANCELADO", "PROMOCIONAL"};
    private final String[] roles = {"CLIENTE", "TECNICO"};

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            Random random = new Random();
            for (int i = 0; i < 15; i++) {
                Notificacion n = new Notificacion();
                n.setTipoNotificacion(tipos[random.nextInt(tipos.length)]);
                n.setTitulo(faker.lorem().sentence(3));
                n.setMensaje(faker.lorem().sentence(10));
                n.setDestinatarioId((long) (1 + random.nextInt(10))); // IDs simulados del 1 al 10
                n.setTipoDestinatario(roles[random.nextInt(roles.length)]);
                n.setFechaEnvio(LocalDateTime.now().minusDays(random.nextInt(7)));
                n.setLeida(random.nextDouble() < 0.3); // 30% leídas
                n.setActiva(true);
                repo.save(n);
            }
            log.info("✅ Se cargaron 15 notificaciones de prueba con Faker");
        }
    }
}