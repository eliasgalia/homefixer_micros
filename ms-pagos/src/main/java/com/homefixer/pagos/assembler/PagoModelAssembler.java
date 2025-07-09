package com.homefixer.pagos.assembler;

import com.homefixer.pagos.controller.PagoController;
import com.homefixer.pagos.model.Pago;
import com.homefixer.pagos.model.EstadoPago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {
    
    @Override
    public EntityModel<Pago> toModel(Pago pago) {
        EntityModel<Pago> pagoModel = EntityModel.of(pago)
                .add(linkTo(methodOn(PagoController.class).obtenerPagoPorId(pago.getIdPago())).withSelfRel())
                .add(linkTo(methodOn(PagoController.class).obtenerTodosLosPagos()).withRel("pagos"));
        
        // Enlaces condicionales según estado
        if (pago.getEstadoPago() == EstadoPago.PENDIENTE) {
            pagoModel.add(linkTo(methodOn(PagoController.class).procesarPago(pago.getIdPago())).withRel("procesar"));
        }
        
        if (pago.getEstadoPago() == EstadoPago.PROCESANDO) {
            pagoModel.add(linkTo(methodOn(PagoController.class).completarPago(pago.getIdPago())).withRel("completar"));
        }
        
        return pagoModel;
    }
}