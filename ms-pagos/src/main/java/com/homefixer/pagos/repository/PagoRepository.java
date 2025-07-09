package com.homefixer.pagos.repository;

import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    List<Pago> findByIdCliente(Long idCliente);
    
    List<Pago> findByIdTecnico(Long idTecnico);
    
    List<Pago> findByEstadoPago(EstadoPago estadoPago);
    
    @Query("SELECT p FROM Pago p WHERE p.idSolicitud = :idSolicitud")
    List<Pago> findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);
    
    @Query("SELECT p FROM Pago p WHERE p.referenciaTransaccion = :referencia")
    Pago findByReferenciaTransaccion(@Param("referencia") String referencia);
}