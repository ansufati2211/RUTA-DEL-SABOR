package com.RutaDelSabor.ruta.models.entities;

import java.math.BigDecimal;
import java.util.Date;
// import java.util.List; // Ya no se necesita
// import com.fasterxml.jackson.annotation.JsonIgnore; // Ya no se necesita
import jakarta.persistence.*;

@Entity
@Table(name = "Producto") // Corregido nombre de tabla
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String Producto; // Nombre del producto
    @Column(precision = 10, scale = 2)
    private BigDecimal Precio;
    private int Stock; // ¡Importante para la lógica de negocio!
    private String Imagen;
    private String Descripcion;
    private boolean AudAnulado;
    private Date CreatedAt;
    private Date UpdatedAt;

    // --- INICIO DE CAMBIOS (Según Guías) ---

    // 1. ELIMINAR ESTA LISTA INCORRECTA
    // @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    // @JsonIgnore
    // private List<Categoria> categorias;

    // 2. ELIMINAR ESTA RELACIÓN INCORRECTA
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "pedidodetallado_id", nullable = true)
    // private PedidoDetallado pedidodetallado;

    // 3. AÑADIR LA REFERENCIA CORRECTA (Un producto pertenece a UNA categoría)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id") // Clave foránea a Categoria
    private Categoria categoria;

    // --- FIN DE CAMBIOS ---

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getProducto() { return Producto; } // Cambiar nombre a 'getNombre' sería mejor práctica
    public void setProducto(String producto) { Producto = producto; } // Cambiar a 'setNombre'
    public BigDecimal getPrecio() { return Precio; }
    public void setPrecio(BigDecimal precio) { Precio = precio; }
    public int getStock() { return Stock; }
    public void setStock(int stock) { Stock = stock; }
    public String getImagen() { return Imagen; }
    public void setImagen(String imagen) { Imagen = imagen; }
    public String getDescripcion() { return Descripcion; }
    public void setDescripcion(String descripcion) { Descripcion = descripcion; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }

    // Getter y Setter para la nueva referencia
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}