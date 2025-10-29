package com.RutaDelSabor.ruta.config;

import com.RutaDelSabor.ruta.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Para especificar métodos HTTP
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Para @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita anotaciones como @PreAuthorize
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Define el codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expone el AuthenticationManager como un Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define la cadena de filtros de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) // Aplica la configuración CORS de WebConfig
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs stateless
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/api/auth/**").permitAll() // Login y Registro
                .requestMatchers(HttpMethod.GET, "/api/menu").permitAll() // Ver menú
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll() // Permitir ver productos individuales (si aplica)
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll() // Permitir ver categorías (si aplica)

                // Rutas de administración (requieren rol ADMIN - A IMPLEMENTAR)
                // .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            // Configura el manejo de sesiones como STATELESS (no se crean sesiones HTTP)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // Añade nuestro filtro JWT antes del filtro estándar de usuario/contraseña
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}