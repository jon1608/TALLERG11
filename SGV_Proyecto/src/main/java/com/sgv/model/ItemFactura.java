package com.sgv.model;

import com.sgv.model.Factura;
import jakarta.persistence.*;

@Entity
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private int cantidad;
    private double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    // Total calculado (no se guarda en la base de datos)
    @Transient
    public double getTotal() {
        return cantidad * precioUnitario;
    }
}
