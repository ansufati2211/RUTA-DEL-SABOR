package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(length = 50)
    private String Medio;

    @Column(columnDefinition = "TEXT") // Para textos más largos
    private String Texto;

    @Column // Asume rating 1-5
    private int Puntuacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date Fecha_Comentario;

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- Relación Corregida ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false) // Debe pertenecer a un cliente
    private Cliente cliente;
    // --- Fin Relación ---

    public Comentario() {}

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        CreatedAt = now;
        UpdatedAt = now;
        if (Fecha_Comentario == null) Fecha_Comentario = now;
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // --- Getters y Setters ---
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getMedio() { return Medio; }
    public void setMedio(String medio) { Medio = medio; }
    public String getTexto() { return Texto; }
    public void setTexto(String texto) { Texto = texto; }
    public int getPuntuacion() { return Puntuacion; }
    public void setPuntuacion(int puntuacion) { Puntuacion = puntuacion; }
    public Date getFecha_Comentario() { return Fecha_Comentario; }
    public void setFecha_Comentario(Date fecha_Comentario) { Fecha_Comentario = fecha_Comentario; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}