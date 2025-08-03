package com.sgv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotNull(message = "La cédula es obligatoria")
    @Positive(message = "La cédula debe ser un número positivo")
    @Column(unique = true)
    private Long cedula;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un correo válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "Debe seleccionar el estado del cliente")
    private String estadoCliente; // Activo / Inactivo

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Factura> facturas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vehiculo> vehiculos;

    // Constructor vacío
    public Cliente() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public Long getCedula() { return cedula; }
    public void setCedula(Long cedula) { this.cedula = cedula; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstadoCliente() { return estadoCliente; }
    public void setEstadoCliente(String estadoCliente) { this.estadoCliente = estadoCliente; }

    public List<Factura> getFacturas() { return facturas; }
    public void setFacturas(List<Factura> facturas) { this.facturas = facturas; }

    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }

    // Utilidad: para mostrar en dropdowns
    @Transient
    public String getIdentificacionConNombre() {
        return cedula + " - " + nombreCompleto;
    }
}
