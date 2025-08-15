package com.sgv.repository;

import com.sgv.model.Pago;
import com.sgv.model.Pago.EstadoPago;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    /** Lista de pagos de una factura (más recientes primero) */
    List<Pago> findByFacturaIdOrderByFechaPagoDesc(Long facturaId);

    /** Suma de pagos por estado (útil para CONFIRMADO) */
    @Query("select coalesce(sum(p.monto), 0) " +
           "from Pago p " +
           "where p.factura.id = :facturaId and p.estado = :estado")
    Double totalPagadoPorEstado(@Param("facturaId") Long facturaId,
                                @Param("estado") EstadoPago estado);

    /** Conveniencia: total pagado confirmado (evita NPEs) */
    default double totalPagadoConfirmado(Long facturaId) {
        Double v = totalPagadoPorEstado(facturaId, EstadoPago.CONFIRMADO);
        return v != null ? v : 0d;
    }
}
