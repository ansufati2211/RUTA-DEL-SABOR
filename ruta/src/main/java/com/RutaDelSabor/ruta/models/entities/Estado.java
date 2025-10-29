package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import jakarta.persistence.*;

// Esta entidad representa UN cambio de estado específico para UN pedido
@Entity
@Table(name = "HistorialEstadoPedido") // Nombre más descriptivo
public class Estado { // Renombrar a HistorialEstadoPedido sería mejor

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String Tipo_Estado; // "RECIBIDO", "EN_PREPARACION", "EN_RUTA", "ENTREGADO", "CANCELADO"

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date FechaHoraCambio; // Momento en que se aplicó este estado

    // Opcional: Notas sobre el cambio de estado
    private String Notas;

    // --- RELACIÓN CORREGIDA ---
    // Este registro de estado pertenece a UN Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false) // Clave foránea a Pedido
    private Pedido pedido;
    // --- FIN RELACIÓN ---

    // Quitar AudAnulado, CreatedAt, UpdatedAt si no son necesarios para un historial
    // private boolean AudAnulado;
	// private Date CreatedAt;
	// private Date UpdatedAt;

    public Estado() {}

    @PrePersist
    protected void onCreate() {
        FechaHoraCambio = new Date(); // Establecer fecha al crear
    }

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getTipo_Estado() { return Tipo_Estado; }
    public void setTipo_Estado(String tipo_Estado) { Tipo_Estado = tipo_Estado; }
    public Date getFechaHoraCambio() { return FechaHoraCambio; }
    // public void setFechaHoraCambio(Date fechaHoraCambio) { FechaHoraCambio = fechaHoraCambio; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
     public String getNotas() { return Notas; }
    public void setNotas(String notas) { Notas = notas; }
}