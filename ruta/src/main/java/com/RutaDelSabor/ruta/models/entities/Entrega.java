package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String Metodo_Entrega; // "Delivery", "Recojo en Local"

    // Este campo no parece pertenecer aquí, el pago está en Pedido o MetodoPago
    // private BigDecimal Metodo_Pago;

    // Podrías añadir campos como: FechaHoraEstimada, FechaHoraReal, EstadoEntrega ("EN_RUTA", "ENTREGADO"), RepartidorID (si aplica)
    private Date FechaHoraEstimada;
    private Date FechaHoraReal;
    private String EstadoEntrega; // Pendiente, En Ruta, Entregado, Cancelado

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- RELACIÓN CORREGIDA ---
    // Una Entrega pertenece a UN Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true) // Único si un pedido solo tiene UNA entrega principal
    private Pedido pedido;
    // --- FIN RELACIÓN ---

    public Entrega() {}

    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
        if (EstadoEntrega == null) EstadoEntrega = "PENDIENTE"; // Estado inicial por defecto
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getMetodo_Entrega() { return Metodo_Entrega; }
    public void setMetodo_Entrega(String metodo_Entrega) { Metodo_Entrega = metodo_Entrega; }
    // public BigDecimal getMetodo_Pago() { return Metodo_Pago; } // Eliminado
    // public void setMetodo_Pago(BigDecimal metodo_Pago) { Metodo_Pago = metodo_Pago; } // Eliminado
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    // public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    // public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    // Getters/Setters para FechaHoraEstimada, FechaHoraReal, EstadoEntrega...
    public Date getFechaHoraEstimada() { return FechaHoraEstimada; }
    public void setFechaHoraEstimada(Date fechaHoraEstimada) { FechaHoraEstimada = fechaHoraEstimada; }
    public Date getFechaHoraReal() { return FechaHoraReal; }
    public void setFechaHoraReal(Date fechaHoraReal) { FechaHoraReal = fechaHoraReal; }
    public String getEstadoEntrega() { return EstadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { EstadoEntrega = estadoEntrega; }
}