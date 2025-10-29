package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    // Campos relacionados con la orden
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date Fecha_Pedido;

    @Column(length = 255) // Dirección de entrega
    private String Direccion;

    @Column(length = 255) // Referencia para entrega
    private String Referencia;

    @Temporal(TemporalType.TIMESTAMP) // Hora estimada de entrega/recojo
    private Date Tiempo_Estimado;

   @Column(precision = 10, scale = 2) // Para valores monetarios
private BigDecimal Subtotal = BigDecimal.ZERO;

@Column(precision = 10, scale = 2) // Costo de envío u otros
private BigDecimal Monto_Agregado = BigDecimal.ZERO;

@Column(precision = 10, scale = 2) // Campo Total calculado
private BigDecimal Total = BigDecimal.ZERO;

    @Column(length = 50) // Estado actual del pedido para acceso rápido
    private String estadoActual = "RECIBIDO"; // Estado inicial por defecto

    // Campos de pago (considerar mover a entidad Pago separada si se complica)
    @Column(length = 4) // Solo últimos 4 dígitos por seguridad PCI DSS básica
    private String ultimosDigitosTarjeta;
    // NO GUARDAR NÚMERO COMPLETO, CVV O FECHA EXP COMPLETA sin cumplir PCI DSS
    @Column(length = 100)
    private String titularTarjeta;
    @Column(length = 20)
    private String numeroYape; // Si se paga con Yape/Plin

    // Campos de auditoría
    @Column(nullable = false)
    private boolean AudAnulado = false;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date CreatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date UpdatedAt;


    // --- Relaciones ---
    // Un pedido pertenece a UN Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false) // Un pedido debe tener cliente
    @JsonIgnore // Evita cargar cliente innecesariamente en listados de pedidos
    private Cliente cliente;

    // Un pedido tiene MUCHOS Detalles (items)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // LAZY por defecto
    private List<PedidoDetallado> detalles = new ArrayList<>();

    // Un pedido tiene UN Comprobante (Boleta/Factura)
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Comprobante comprobante;

    // Un pedido tiene UNA instancia de pago
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private MetodoPago pago;

    // Un pedido tiene UNA instancia de entrega
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Entrega entrega;

    // Un pedido tiene UN historial de Estados
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("FechaHoraCambio DESC") // El más reciente primero
    private List<Estado> historialEstados = new ArrayList<>();

    // Opcional: Un empleado gestiona/prepara el pedido
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "empleado_gestor_id")
    // private Empleado empleadoGestor;

    // --- Constructor y Timestamps ---
    public Pedido() {}

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        CreatedAt = now;
        UpdatedAt = now;
        if (Fecha_Pedido == null) Fecha_Pedido = now; // Fecha de pedido por defecto
        if (estadoActual == null) estadoActual = "RECIBIDO"; // Estado inicial
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = new Date();
    }

    // --- Métodos de ayuda (Ejemplo: Añadir estado al historial) ---
    public void addEstadoHistorial(Estado nuevoEstado) {
        if (this.historialEstados == null) {
            this.historialEstados = new ArrayList<>();
        }
        nuevoEstado.setPedido(this); // Asegurar la relación bidireccional
        this.historialEstados.add(nuevoEstado);
        this.estadoActual = nuevoEstado.getTipo_Estado(); // Actualizar estado actual
        this.UpdatedAt = new Date(); // Forzar actualización de timestamp del pedido
    }


    // --- Getters y Setters ---
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public Date getFecha_Pedido() { return Fecha_Pedido; }
    public void setFecha_Pedido(Date fecha_Pedido) { Fecha_Pedido = fecha_Pedido; }
    public String getDireccion() { return Direccion; }
    public void setDireccion(String direccion) { Direccion = direccion; }
    public String getReferencia() { return Referencia; }
    public void setReferencia(String referencia) { Referencia = referencia; }
    public Date getTiempo_Estimado() { return Tiempo_Estimado; }
    public void setTiempo_Estimado(Date tiempo_Estimado) { Tiempo_Estimado = tiempo_Estimado; }
    public BigDecimal getSubtotal() { return Subtotal; }
    public void setSubtotal(BigDecimal subtotal) { Subtotal = subtotal; }
    public BigDecimal getMonto_Agregado() { return Monto_Agregado; }
    public void setMonto_Agregado(BigDecimal monto_Agregado) { Monto_Agregado = monto_Agregado; }
    public BigDecimal getTotal() { return Total; } // Cambiado a BigDecimal
    public void setTotal(BigDecimal total) { Total = total; } // Cambiado a BigDecimal
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public String getUltimosDigitosTarjeta() { return ultimosDigitosTarjeta; }
    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) { this.ultimosDigitosTarjeta = ultimosDigitosTarjeta; }
    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }
    public String getNumeroYape() { return numeroYape; }
    public void setNumeroYape(String numeroYape) { this.numeroYape = numeroYape; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<PedidoDetallado> getDetalles() { return detalles; }
    public void setDetalles(List<PedidoDetallado> detalles) { this.detalles = detalles; }
    public Comprobante getComprobante() { return comprobante; }
    public void setComprobante(Comprobante comprobante) { this.comprobante = comprobante; }
    public MetodoPago getPago() { return pago; }
    public void setPago(MetodoPago pago) { this.pago = pago; }
    public Entrega getEntrega() { return entrega; }
    public void setEntrega(Entrega entrega) { this.entrega = entrega; }
    public List<Estado> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<Estado> historialEstados) { this.historialEstados = historialEstados; }
    // Getter/Setter para empleadoGestor si lo implementas...
}