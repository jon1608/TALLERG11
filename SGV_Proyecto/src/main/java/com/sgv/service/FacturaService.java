package com.sgv.service;

import com.sgv.model.Factura;
import com.sgv.model.ItemFactura;
import com.sgv.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public void guardar(Factura factura) {
        System.out.println("Calculando totales de factura: " + factura.getItems().size() + " items");

        double subtotal = 0;
        for (ItemFactura item : factura.getItems()) {
            System.out.println("Item: " + item.getDescripcion() + ", cantidad: " + item.getCantidad() + ", precio: " + item.getPrecioUnitario());
            double totalItem = item.getCantidad() * item.getPrecioUnitario();
            subtotal += totalItem;
        }

        double iva = subtotal * 0.13;
        double total = subtotal + iva;

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);

        facturaRepository.save(factura);
    }

        public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }
}
