package com.RutaDelSabor.ruta.models.entities;

import java.util.Date;
import java.util.List; // Importar List
import com.fasterxml.jackson.annotation.JsonIgnore; // Importar JsonIgnore
import jakarta.persistence.*;

@Entity
@Table(name = "Categoria") // Corregido nombre de tabla
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String Categoria; // Nombre de la categoría, considerar cambiar a 'nombre'
    private String Icono;
    private boolean AudAnulado;
    private Date CreatedAt;
    private Date UpdatedAt;

    // --- INICIO DE CAMBIOS (Según Guías) ---

    // 1. ELIMINAR ESTA RELACIÓN INCORRECTA
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "producto_id", nullable = true)
    // private Producto producto;

    // 2. AÑADIR LA LISTA DE PRODUCTOS (Una categoría tiene MUCHOS productos)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Evita bucles al serializar
    private List<Producto> productos;

    // --- FIN DE CAMBIOS ---

    // Getters y Setters...
    public Long getID() { return ID; }
    public void setID(Long iD) { ID = iD; }
    public String getCategoria() { return Categoria; } // Cambiar a 'getNombre'
    public void setCategoria(String categoria) { Categoria = categoria; } // Cambiar a 'setNombre'
    public String getIcono() { return Icono; }
    public void setIcono(String icono) { Icono = icono; }
    public boolean isAudAnulado() { return AudAnulado; }
    public void setAudAnulado(boolean audAnulado) { AudAnulado = audAnulado; }
    public Date getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(Date createdAt) { CreatedAt = createdAt; }
    public Date getUpdatedAt() { return UpdatedAt; }
    public void setUpdatedAt(Date updatedAt) { UpdatedAt = updatedAt; }

    // Getter y Setter para la nueva lista
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}