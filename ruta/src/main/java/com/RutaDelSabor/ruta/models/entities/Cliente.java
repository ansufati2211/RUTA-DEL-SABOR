// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/models/entities/Cliente.java

package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "Cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, length = 50)
    private String Nombre;

    @Column(nullable = false, length = 50)
    private String Apellido;

    @Column(nullable = false, unique = true, length = 100)
    private String Correo;

    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String Contraseña;

    @Column(length = 15)
    private String Telefono;

    @Temporal(TemporalType.DATE)
    private Date Fecha_Nacimiento;

    @Column(length = 222)
    private String Direccion;

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- Relaciones ---
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comentario> comentarios = new ArrayList<>();

    // --- INICIO DE LA CORRECCIÓN ---
    // Un Cliente pertenece a UN Rol
    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que el rol se cargue siempre con el cliente para la seguridad
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    // --- FIN DE LA CORRECCIÓN ---


    public Cliente() {}

    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // --- Getters y Setters ---

    // ... (getters y setters existentes para ID, Nombre, Apellido, etc.)
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getNombre() { return Nombre; }
    public void setNombre(String nombre) { Nombre = nombre; }
    public String getApellido() { return Apellido; }
    public void setApellido(String apellido) { Apellido = apellido; }
    public String getCorreo() { return Correo; }
    public void setCorreo(String correo) { Correo = correo; }
    public String getContraseña() { return Contraseña; }
    public void setContraseña(String contraseña) { Contraseña = contraseña; }
    public String getTelefono() { return Telefono; }
    public void setTelefono(String telefono) { Telefono = telefono; }
    public Date getFecha_Nacimiento() { return Fecha_Nacimiento; }
    public void setFecha_Nacimiento(Date fecha_Nacimiento) { Fecha_Nacimiento = fecha_Nacimiento; }
    public String getDireccion() { return Direccion; }
    public void setDireccion(String direccion) { Direccion = direccion; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }


    // --- INICIO DE LA CORRECCIÓN (MÉTODOS FALTANTES) ---
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    // --- FIN DE LA CORRECCIÓN ---
}