package com.sgv.service;

import com.sgv.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {

    /** Crea y guarda un pago (valida monto > 0). */
    Pago registrar(Pago pago);

    /** Pagos de una factura (recientes primero). */
    List<Pago> listarPorFactura(Long facturaId);

    /** Total pagado (CONFIRMADO) de una factura. */
    double totalPagadoConfirmado(Long facturaId);

    /** Busca un pago por id. */
    Optional<Pago> obtenerPorId(Long id);

    /** Cambia estado de un pago (por ejemplo, ANULADO). */
    Pago actualizarEstado(Long pagoId, Pago.EstadoPago nuevoEstado);
}
