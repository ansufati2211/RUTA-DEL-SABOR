package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import java.util.List; // Importar List
import com.fasterxml.jackson.annotation.JsonIgnore; // Importar JsonIgnore
import jakarta.persistence.*;

@Entity
// Renombrar tabla y clase a "TipoDocumento" sería más claro
@Table(name = "TipoDocumento")
public class Documento { // Renombrar clase a TipoDocumento

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, unique = true) // El tipo debe ser único y no nulo
    private String Tipo_Documento; // "DNI", "CE", "Pasaporte"

    // Podrías añadir descripción, longitud esperada del número, etc.

    @Column(nullable = false)
    private boolean AudAnulado = false; // Valor por defecto

    @Temporal(TemporalType.TIMESTAMP) // Especificar que guarda fecha y hora
    @Column(nullable = false, updatable = false) // No nulo, no actualizable
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) // No nulo
    private Date UpdatedAt;

    // --- RELACIÓN CORREGIDA ---
    // Un TipoDocumento puede estar asociado a MUCHOS empleados
    @OneToMany(mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    @JsonIgnore // Evitar bucles al serializar empleados
    private List<Empleado> empleados;
    // --- FIN RELACIÓN ---

    // JPA necesita un constructor vacío
    public Documento() {}

    // Método para manejar timestamps automáticamente antes de persistir/actualizar
    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date(); // Asegurar que UpdatedAt también se establezca al crear
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getTipo_Documento() { return Tipo_Documento; }
    public void setTipo_Documento(String tipo_Documento) { Tipo_Documento = tipo_Documento; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    // No necesitamos setCreatedAt si usamos @PrePersist
    // public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    // No necesitamos setUpdatedAt si usamos @PrePersist/@PreUpdate
    // public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }
    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }
}