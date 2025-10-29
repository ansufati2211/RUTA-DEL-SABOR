package com.RutaDelSabor.ruta.models.entities;

import java.math.BigDecimal;
import java.util.Date;
// import java.util.List; // No necesita listas
import jakarta.persistence.*;

@Entity
@Table(name = "Pedido_Detallado") // Corregido nombre de tabla
public class PedidoDetallado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private int Cantidad;
    @Column(precision = 10, scale = 2)
    private BigDecimal Subtotal;
    private boolean AudAnulado;
    private Date CreatedAt;
    private Date UpdatedAt;

    // --- INICIO DE CAMBIOS (Según Guías) ---

    // 2. AÑADIR LAS REFERENCIAS CORRECTAS (A UN Pedido y UN Producto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false) // Clave foránea a Pedido
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false) // Clave foránea a Producto
    private Producto producto;

    // --- FIN DE CAMBIOS ---

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public int getCantidad() { return Cantidad; }
    public void setCantidad(int cantidad) { Cantidad = cantidad; }
    public BigDecimal getSubtotal() { return Subtotal; }
    public void setSubtotal(BigDecimal subtotal) { Subtotal = subtotal; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }

    // Getters y Setters para las nuevas referencias
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
