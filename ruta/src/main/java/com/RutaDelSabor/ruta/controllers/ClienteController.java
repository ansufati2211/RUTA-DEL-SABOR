package com.RutaDelSabor.ruta.controllers;

import java.util.List;
import com.RutaDelSabor.ruta.dto.ErrorResponseDTO;
import com.RutaDelSabor.ruta.exception.RecursoNoEncontradoException; // Usar excepción
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Cliente; // Cambiar entidad
import com.RutaDelSabor.ruta.services.IClienteService; // Cambiar servicio
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes") // Cambiar ruta base
// @CrossOrigin(origins = "*") // Mejor en WebConfig
public class ClienteController { // Cambiar nombre

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class); // Cambiar clase

    @Autowired
    private IClienteService clienteService; // Cambiar servicio

    // --- Endpoints Públicos (si aplica, por ejemplo, no aplica para Clientes) ---
    // GET /api/clientes - Probablemente no debería ser público

    // --- Endpoints de Administración ---

    // GET /api/clientes/admin/all (Lista completa para admin)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')") // Proteger endpoint
    public ResponseEntity<List<Cliente>> getAllAdmin() {
        log.info("Admin: Solicitud GET /api/clientes/admin/all"); // Cambiar log
        List<Cliente> clientes = clienteService.buscarTodos(); // Cambiar método servicio
        return ResponseEntity.ok(clientes);
    }

    // GET /api/clientes/admin/{id} (Detalle para admin)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByIdAdmin(@PathVariable Long id) {
         log.info("Admin: Solicitud GET /api/clientes/admin/{}", id); // Cambiar log
         try {
            Cliente cliente = clienteService.buscarPorId(id); // Cambiar método servicio
            return ResponseEntity.ok(cliente);
         } catch (RecursoNoEncontradoException e) { // Usar excepción
             log.warn("Admin: Cliente no encontrado: {}", e.getMessage()); // Cambiar log
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
         } catch (Exception e) {
             log.error("Admin: Error al obtener cliente ID {}:", id, e); // Cambiar log
             return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al obtener el cliente.")); // Cambiar mensaje
         }
    }

    // POST /api/clientes/admin (Crear nuevo) - Generalmente se usa /register, pero si admin crea...
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente) { // Cambiar entidad y variable
        log.info("Admin: Solicitud POST /api/clientes/admin"); // Cambiar log
         if (cliente.getID() != null) {
              return ResponseEntity.badRequest().body(new ErrorResponseDTO("No incluya ID al crear."));
         }
        try {
            // Considerar si la contraseña debe venir hasheada o si el servicio la hashea
            Cliente nuevoCliente = clienteService.guardar(cliente); // Cambiar servicio y variable
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (Exception e) { // Capturar errores específicos (ej: correo duplicado)
             log.error("Admin: Error al crear cliente:", e); // Cambiar log
             // Devolver mensaje más específico si es posible (ej: DataIntegrityViolationException)
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al crear el cliente: " + e.getMessage())); // Cambiar mensaje
        }
    }

    // PUT /api/clientes/admin/{id} (Actualizar)
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteDetalles) { // Cambiar entidad y variable
         log.info("Admin: Solicitud PUT /api/clientes/admin/{}", id); // Cambiar log
         try {
            Cliente clienteExistente = clienteService.buscarPorId(id); // Cambiar servicio y variable

            // Copiar campos actualizables (evitar sobreescribir ID, CreatedAt, Contraseña si no se provee)
            clienteExistente.setNombre(clienteDetalles.getNombre());
            clienteExistente.setApellido(clienteDetalles.getApellido());
            clienteExistente.setCorreo(clienteDetalles.getCorreo()); // Validar unicidad si cambia?
            clienteExistente.setTelefono(clienteDetalles.getTelefono());
            clienteExistente.setFecha_Nacimiento(clienteDetalles.getFecha_Nacimiento());
            clienteExistente.setDireccion(clienteDetalles.getDireccion());
            // No actualizar contraseña aquí a menos que sea un endpoint específico para ello
            // clienteExistente.setAudAnulado(false); // Podría reactivar si estaba anulado?

            Cliente actualizado = clienteService.guardar(clienteExistente); // Cambiar servicio y variable
            return ResponseEntity.ok(actualizado);
         } catch (RecursoNoEncontradoException e) { // Usar excepción
             log.warn("Admin: Cliente no encontrado para actualizar: {}", e.getMessage()); // Cambiar log
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
         } catch (Exception e) {
             log.error("Admin: Error al actualizar cliente ID {}:", id, e); // Cambiar log
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar el cliente: " + e.getMessage())); // Cambiar mensaje
         }
    }

    // DELETE /api/clientes/admin/{id} (Borrado lógico)
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) { // Cambiar nombre método
         log.warn("Admin: Solicitud DELETE /api/clientes/admin/{}", id); // Cambiar log
         try {
            clienteService.eliminarLogico(id); // Cambiar servicio
             log.info("Admin: Borrado lógico de cliente ID {} completado.", id); // Cambiar log
            return ResponseEntity.noContent().build();
         } catch (RecursoNoEncontradoException e) { // Capturar si no se encontró para borrar
             log.error("Admin: Error al borrar, cliente ID {} no encontrado.", id); // Cambiar log
             // Devolver 204 incluso si no existía, la meta (que no exista) se cumple
             return ResponseEntity.noContent().build();
         } catch (Exception e) {
             log.error("Admin: Error inesperado al borrar cliente ID {}:", id, e); // Cambiar log
             return ResponseEntity.internalServerError().build();
         }
    }
}
