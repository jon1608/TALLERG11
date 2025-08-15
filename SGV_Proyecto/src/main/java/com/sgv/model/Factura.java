package com.sgv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha = LocalDate.now();

    private double subtotal;
    private double iva;
    private double total;

    @NotNull(message = "Debe seleccionar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Debe seleccionar una placa")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFactura> items = new ArrayList<>();

    /* ===== NUEVO: relación con pagos ===== */
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    /* ===== Getters/Setters ===== */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public List<ItemFactura> getItems() { return items; }
    public void setItems(List<ItemFactura> items) {
        this.items = items;
        if (items != null) {
            for (ItemFactura item : items) item.setFactura(this);
        }
    }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
        if (pagos != null) {
            for (Pago p : pagos) p.setFactura(this);
        }
    }
    public void addPago(Pago pago) {
        if (pago != null) {
            pago.setFactura(this);
            this.pagos.add(pago);
        }
    }

    /* ===== Conveniencia (no mapeados) ===== */

    @Transient
    public double getTotalPagado() {
        if (pagos == null) return 0d;
        // Sumamos solo pagos confirmados
        return pagos.stream()
                .filter(p -> p.getEstado() == Pago.EstadoPago.CONFIRMADO)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    @Transient
    public double getSaldoPendiente() {
        return Math.max(0d, total - getTotalPagado());
    }
}
