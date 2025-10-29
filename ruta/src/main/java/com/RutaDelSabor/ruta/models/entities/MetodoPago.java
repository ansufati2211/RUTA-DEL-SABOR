package com.RutaDelSabor.ruta.models.entities;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "MetodoPago") // Podría ser PagoPedido si es la instancia del pago
public class MetodoPago { // O PagoPedido

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String Metodo_Pago; // "Tarjeta", "Yape", "Efectivo"

    // Añadir campos relevantes: Monto, FechaPago, EstadoPago ("PENDIENTE", "PAGADO", "FALLIDO")
    @Column(precision = 10, scale = 2)
    private BigDecimal Monto;
    private Date FechaPago;
    private String EstadoPago;

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- RELACIÓN CORREGIDA ---
    // Esta instancia de pago pertenece a UN Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true) // Único si un pedido solo tiene UN método/instancia de pago
    private Pedido pedido;
    // --- FIN RELACIÓN ---

    public MetodoPago() {}

    @PrePersist
    protected void onCreate() {
        CreatedAt = new Date();
        UpdatedAt = new Date();
        if (EstadoPago == null) EstadoPago = "PENDIENTE"; // Estado inicial
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getMetodo_Pago() { return Metodo_Pago; }
    public void setMetodo_Pago(String metodo_Pago) { Metodo_Pago = metodo_Pago; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    // public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    // public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    // Getters/Setters para Monto, FechaPago, EstadoPago...
     public BigDecimal getMonto() { return Monto; }
    public void setMonto(BigDecimal monto) { Monto = monto; }
    public Date getFechaPago() { return FechaPago; }
    public void setFechaPago(Date fechaPago) { FechaPago = fechaPago; }
    public String getEstadoPago() { return EstadoPago; }
    public void setEstadoPago(String estadoPago) { EstadoPago = estadoPago; }
}