package com.homefixer.config;

import com.github.javafaker.Faker;
import com.homefixer.model.Valoracion;
import com.homefixer.repository.ValoracionRepository;
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

    private final ValoracionRepository repo;
    private final Faker faker = new Faker(Locale.forLanguageTag("es"));
    private final Random rnd = new Random();

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            for (int i = 0; i < 15; i++) {
                Valoracion v = Valoracion.builder()
                    .idSolicitud((long) (1 + rnd.nextInt(10)))
                    .idCliente((long) (1 + rnd.nextInt(5)))
                    .idTecnico((long) (1 + rnd.nextInt(5)))
                    .calificacion(1 + rnd.nextInt(5))
                    .comentario(faker.lorem().sentence(10))
                    .fechaValoracion(LocalDateTime.now().minusDays(rnd.nextInt(7)))
                    .calificacionPuntualidad(1 + rnd.nextInt(5))
                    .calificacionCalidad(1 + rnd.nextInt(5))
                    .calificacionComunicacion(1 + rnd.nextInt(5))
                    .recomendaria(rnd.nextBoolean())
                    .build();
                repo.save(v);
            }
            log.info("✅ Se cargaron 15 valoraciones de prueba.");
        }
    }
}