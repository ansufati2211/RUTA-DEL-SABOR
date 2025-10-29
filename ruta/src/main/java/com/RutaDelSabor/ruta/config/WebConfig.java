package com.RutaDelSabor.ruta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica a todas las rutas bajo /api
                // ¡IMPORTANTE! Reemplaza con las URLs reales de tu frontend
                .allowedOrigins("https://vermillion-muffin-71a41f.netlify.app", "http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:4200") // Añadir todos los orígenes necesarios (Netlify, local dev, Angular local si aplica)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Cabeceras permitidas (incluyendo Authorization)
                .allowCredentials(true) // Permite enviar cookies/tokens
                .maxAge(3600); // Tiempo que el navegador cachea la respuesta pre-flight OPTIONS
    }
}