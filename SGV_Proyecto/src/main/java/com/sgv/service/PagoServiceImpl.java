package com.sgv.service;

import com.sgv.model.Factura;
import com.sgv.model.Pago;
import com.sgv.repository.FacturaRepository;
import com.sgv.repository.PagoRepository;
import com.sgv.service.PagoService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final FacturaRepository facturaRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, FacturaRepository facturaRepository) {
        this.pagoRepository = pagoRepository;
        this.facturaRepository = facturaRepository;
    }

    @Override
    public Pago registrar(Pago pago) {
        if (pago == null) throw new IllegalArgumentException("Pago requerido");
        if (pago.getMonto() <= 0) throw new IllegalArgumentException("El monto debe ser mayor a cero");
        if (pago.getFactura() == null || pago.getFactura().getId() == null) {
            throw new IllegalArgumentException("Debe asociar una factura válida");
        }

        // Validar factura existente
        Factura factura = facturaRepository.findById(pago.getFactura().getId())
            .orElseThrow(() -> new IllegalArgumentException("Factura no existe"));

        // Evitar sobrepago cuando viene CONFIRMADO
        if (pago.getEstado() == Pago.EstadoPago.CONFIRMADO) {
            double pagado = totalPagadoConfirmado(factura.getId());
            double saldoPendiente = Math.max(0d, factura.getTotal() - pagado);
            if (pago.getMonto() > saldoPendiente) {
                throw new IllegalArgumentException("El pago excede el saldo pendiente");
            }
        }

        pago.setFactura(factura); // asociar entidad administrada
        return pagoRepository.save(pago);
    }

    @Override
    public List<Pago> listarPorFactura(Long facturaId) {
        return pagoRepository.findByFacturaIdOrderByFechaPagoDesc(facturaId);
    }

    @Override
    public double totalPagadoConfirmado(Long facturaId) {
        return pagoRepository.totalPagadoConfirmado(facturaId);
    }

    @Override
    public Optional<Pago> obtenerPorId(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    public Pago actualizarEstado(Long pagoId, Pago.EstadoPago nuevoEstado) {
        Pago pago = pagoRepository.findById(pagoId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        pago.setEstado(nuevoEstado);
        return pagoRepository.save(pago);
    }
}
