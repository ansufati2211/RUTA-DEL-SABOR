package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, unique = true) // Nombre del rol debe ser único
    private String Name; // "ADMIN", "VENDEDOR", "CLIENTE" (¿Necesitas rol cliente?)

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- RELACIÓN CORREGIDA ---
    // Un Rol puede tener MUCHOS Empleados
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Evitar bucles
    private List<Empleado> empleados; // Renombrado de 'empleado' a 'empleados'
    // --- FIN RELACIÓN ---

    public Rol() {}

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
    public String getName() { return Name; }
    public void setName(String name) { Name = name; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    // public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    // public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }
    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }
}