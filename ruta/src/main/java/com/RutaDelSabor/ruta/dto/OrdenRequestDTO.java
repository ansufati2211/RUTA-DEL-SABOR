// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/dto/OrdenRequestDTO.java

package com.RutaDelSabor.ruta.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty; // Para validar la lista de items

public class OrdenRequestDTO {

    @NotEmpty(message = "La lista de items no puede estar vacía.")
    private List<ItemDTO> items;

    // Datos del cliente
    private String nombreCliente;
    private String apellidoCliente;
    private String correoCliente;
    private String dniCliente;
    private String telefonoCliente; // Cambiado a String para consistencia
    private String tipoComprobante; // "Boleta" o "Factura"

    // Datos de entrega
    private String tipoEntrega; // "Delivery" o "Recojo en Local"
    private String direccionEntrega;
    private String referenciaEntrega;

    // Datos de pago
    private String metodoPago; // "Tarjeta" o "Yape"
    private String numeroTarjeta;
    private String fechaVencimiento; // MM/YY
    private String cvv;
    private String titularTarjeta;
    private String numeroYape;

    // --- INICIO DE LA CORRECCIÓN: GETTERS Y SETTERS COMPLETOS ---

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(String tipoEntrega) { this.tipoEntrega = tipoEntrega; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getReferenciaEntrega() { return referenciaEntrega; }
    public void setReferenciaEntrega(String referenciaEntrega) { this.referenciaEntrega = referenciaEntrega; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }

    public String getNumeroYape() { return numeroYape; }
    public void setNumeroYape(String numeroYape) { this.numeroYape = numeroYape; }

    // --- FIN DE LA CORRECCIÓN ---
}