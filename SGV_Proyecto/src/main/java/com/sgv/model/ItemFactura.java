package com.sgv.model;

import jakarta.persistence.*;

@Entity
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private int cantidad;
    private double precioUnitario;
    private double total;

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

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
