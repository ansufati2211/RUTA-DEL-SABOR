package com.RutaDelSabor.ruta.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, length = 20) // Boleta, Factura
    private String Tipo_Comprobante;

    @Column(length = 10) // Ej: B001, F001
    private String Serie;

    @Column(length = 20) // Ej: 00012345
    private String Numero;

    @Temporal(TemporalType.TIMESTAMP)
    private Date FechaEmision;

   @Column(precision = 10, scale = 2) // Monto total del comprobante
    private BigDecimal MontoTotal;

    @Column(length = 11) // RUC para facturas
    private String Ruc;

    @Column(nullable = false)
    private boolean AudAnulado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;

    // --- Relación Corregida ---
    @OneToOne(fetch = FetchType.LAZY) // Asumiendo un comprobante por pedido
    @JoinColumn(name = "pedido_id", nullable = false, unique = true) // Clave foránea única
    private Pedido pedido;
    // --- Fin Relación ---

    public Comprobante() {}

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        CreatedAt = now;
        UpdatedAt = now;
        if (FechaEmision == null) FechaEmision = now;
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // --- Getters y Setters ---
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getTipo_Comprobante() { return Tipo_Comprobante; }
    public void setTipo_Comprobante(String tipo_Comprobante) { Tipo_Comprobante = tipo_Comprobante; }
    public String getSerie() { return Serie; }
    public void setSerie(String serie) { Serie = serie; }
    public String getNumero() { return Numero; }
    public void setNumero(String numero) { Numero = numero; }
    public Date getFechaEmision() { return FechaEmision; }
    public void setFechaEmision(Date fechaEmision) { FechaEmision = fechaEmision; }
    public BigDecimal getMontoTotal() { return MontoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { MontoTotal = montoTotal; }
    public String getRuc() { return Ruc; }
    public void setRuc(String ruc) { Ruc = ruc; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}