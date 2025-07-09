package com.homefixer.pagos.config;

import com.github.javafaker.Faker;
import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import com.homefixer.pagos.model.MetodoPago;
import com.homefixer.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final PagoRepository pagoRepository;
    private final Faker faker = new Faker(new Locale("es"));
    
    @Override
    public void run(String... args) throws Exception {
        if (pagoRepository.count() == 0) {
            cargarDatosPrueba();
        }
    }
    
    private void cargarDatosPrueba() {
        System.out.println("Cargando datos de prueba para pagos...");
        
        EstadoPago[] estados = EstadoPago.values();
        MetodoPago[] metodos = MetodoPago.values();
        
        for (int i = 1; i <= 15; i++) {
            Pago pago = new Pago();
            pago.setIdSolicitud(faker.number().numberBetween(1L, 20L));
            pago.setIdCliente(faker.number().numberBetween(1L, 10L));
            pago.setIdTecnico(faker.number().numberBetween(1L, 10L));
            pago.setMonto(new BigDecimal(faker.number().numberBetween(20000, 200000)));
            pago.setMetodoPago(metodos[faker.number().numberBetween(0, metodos.length)]);
            pago.setEstadoPago(estados[faker.number().numberBetween(0, estados.length)]);
            pago.setDescripcion("Pago por servicio de " + faker.commerce().department());
            pago.setReferenciaTransaccion("PAY-" + faker.number().digits(10));
            pago.setFechaCreacion(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            
            if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
                pago.setFechaProcesamiento(pago.getFechaCreacion().plusHours(faker.number().numberBetween(1, 24)));
            }
            
            pagoRepository.save(pago);
        }
        
        System.out.println("15 pagos de prueba cargados exitosamente.");
    }
}