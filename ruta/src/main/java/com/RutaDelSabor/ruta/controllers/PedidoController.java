// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/controllers/PedidoController.java

package com.RutaDelSabor.ruta.controllers;

import java.util.List;
import java.util.stream.Collectors;


import com.RutaDelSabor.ruta.dto.ErrorResponseDTO;
import com.RutaDelSabor.ruta.dto.EstadoResponseDTO;
import com.RutaDelSabor.ruta.dto.OrdenRequestDTO;
import com.RutaDelSabor.ruta.dto.OrdenResponseDTO;
import com.RutaDelSabor.ruta.dto.PedidoHistorialDTO;
import com.RutaDelSabor.ruta.exception.PedidoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.ProductoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.StockInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.services.IPedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private IPedidoService pedidoService;

    // --- ENDPOINT CREAR ORDEN ---
    @PostMapping("/ordenes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> crearOrden(
            @Valid @RequestBody OrdenRequestDTO ordenRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición POST /api/ordenes por usuario: {}", userDetails.getUsername());
        try {
            Pedido nuevoPedido = pedidoService.crearNuevaOrden(ordenRequest, userDetails);
            log.info("Orden creada exitosamente con ID: {}", nuevoPedido.getID());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new OrdenResponseDTO(nuevoPedido.getID(),
                            "Pedido recibido exitosamente con ID: " + nuevoPedido.getID()));
        } catch (StockInsuficienteException | ProductoNoEncontradoException e) {
            log.warn("Error de validación al crear orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear orden:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error interno al procesar el pedido. Intente más tarde."));
        }
    }

    // --- ENDPOINT OBTENER ESTADO ---
    @GetMapping("/ordenes/{id}/estado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getEstadoOrden(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición GET /api/ordenes/{}/estado por usuario: {}", id, userDetails.getUsername());
        try {
            String estado = pedidoService.obtenerEstadoPedido(id, userDetails);
            log.info("Estado obtenido para pedido ID {}: {}", id, estado);
            return ResponseEntity.ok(new EstadoResponseDTO(estado));
        } catch (PedidoNoEncontradoException e) {
            log.warn("Pedido no encontrado o sin acceso para usuario {}: {}", userDetails.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener estado del pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener el estado del pedido."));
        }
    }

    // --- ENDPOINT OBTENER HISTORIAL ---
    @GetMapping("/clientes/me/historial")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getHistorialPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición GET /api/clientes/me/historial por usuario: {}", userDetails.getUsername());
        try {
            List<Pedido> historial = pedidoService.obtenerHistorialPedidos(userDetails);
            List<PedidoHistorialDTO> historialDTO = historial.stream()
                    .map(PedidoHistorialDTO::fromEntity)
                    .collect(Collectors.toList());
            log.info("Historial obtenido para usuario {}: {} pedidos.", userDetails.getUsername(), historialDTO.size());
            return ResponseEntity.ok(historialDTO);
        } catch (Exception e) {
            log.error("Error al obtener historial para usuario {}:", userDetails.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener el historial de pedidos."));
        }
    }

    // --- Endpoints CRUD (Probablemente solo para ADMIN) ---

    @GetMapping("/pedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pedido> getAll() {
        log.info("Admin: Obteniendo todos los pedidos.");
        return pedidoService.GetAll();
    }

    @GetMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPedidoByIdAdmin(@PathVariable Long id) {
        log.info("Admin: Obteniendo pedido ID: {}", id);
        try {
            Pedido pedido = pedidoService.FindByID(id);
            return ResponseEntity.ok(pedido);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePedidoAdmin(@PathVariable Long id, @RequestBody Pedido pedidoActualizado) {
        log.info("Admin: Actualizando pedido ID: {}", id);
        try {
            Pedido pedidoExistente = pedidoService.FindByID(id); // Verifica si existe
            
            // --- INICIO DE LA CORRECCIÓN ---
            // Validar y copiar campos actualizables.
            // Por ejemplo, si el admin puede cambiar el estado:
            if (pedidoActualizado.getEstadoActual() != null) {
                pedidoExistente.setEstadoActual(pedidoActualizado.getEstadoActual());
            }
            // La línea 'pedidoExistente.setUpdatedAt(new Date());' se elimina porque @PreUpdate lo hace automáticamente.
            // --- FIN DE LA CORRECCIÓN ---

            Pedido guardado = pedidoService.Save(pedidoExistente); // Al guardar aquí, @PreUpdate se disparará.
            return ResponseEntity.ok(guardado);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin: Error al actualizar pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar pedido."));
        }
    }

    @DeleteMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePedidoAdmin(@PathVariable Long id) {
        log.warn("Admin: Solicitando borrado lógico de pedido ID: {}", id);
        try {
            pedidoService.Delete(id);
            log.info("Admin: Borrado lógico de pedido ID {} completado.", id);
            return ResponseEntity.noContent().build();
        } catch (PedidoNoEncontradoException e) {
            log.error("Admin: Error al borrar, pedido ID {} no encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Admin: Error inesperado al borrar pedido ID {}:", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}