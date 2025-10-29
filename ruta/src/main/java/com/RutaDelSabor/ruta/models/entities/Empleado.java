// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/models/entities/Empleado.java

package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
// import java.util.List; // Eliminado
// import com.fasterxml.jackson.annotation.JsonIgnore; // Eliminado
import jakarta.persistence.*;

@Entity
@Table(name = "Empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String Nombre_Empleado;

    @Column(nullable = false, unique = true)
    private String Numero_Documento;

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private Documento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Empleado() {}

    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getNombre_Empleado() { return Nombre_Empleado; }
    public void setNombre_Empleado(String nombre_Empleado) { Nombre_Empleado = nombre_Empleado; }
    public String getNumero_Documento() { return Numero_Documento; }
    public void setNumero_Documento(String numero_Documento) { Numero_Documento = numero_Documento; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public Documento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(Documento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}