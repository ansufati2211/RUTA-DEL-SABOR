package com.RutaDelSabor.ruta.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank @Email
    private String correo;
    @NotBlank
    private String contraseña; // Cambiar a 'password'

    // Getters y Setters
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}