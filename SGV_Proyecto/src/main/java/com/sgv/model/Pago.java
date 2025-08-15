package com.sgv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El monto es obligatorio")
    private double monto;

    // Después (mapea al nombre real de la columna):
    @NotNull(message = "La fecha de pago es obligatoria")
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    /** Nº SINPE, últimos 4 dígitos de tarjeta, nº transferencia, etc. (opcional) */
    @Column(length = 120)
    private String referencia;

    /** Nota libre del cajero/usuario (opcional) */
    @Column(length = 255)
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    /* ===== ENUMS ===== */
    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA, SINPE   // ← si no usas SINPE, puedes quitarlo
    }

    public enum EstadoPago {
        PENDIENTE, CONFIRMADO, CANCELADO
    }

    /* ===== Getters/Setters ===== */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
