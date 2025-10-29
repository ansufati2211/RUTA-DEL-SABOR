package com.RutaDelSabor.ruta.services;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.RutaDelSabor.ruta.dto.ItemDTO;
import com.RutaDelSabor.ruta.dto.OrdenRequestDTO;
import com.RutaDelSabor.ruta.exception.ProductoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.StockInsuficienteException;
import com.RutaDelSabor.ruta.exception.PedidoNoEncontradoException;
import com.RutaDelSabor.ruta.models.dao.IClienteDAO;
import com.RutaDelSabor.ruta.models.dao.IPedidoDAO;
import com.RutaDelSabor.ruta.models.dao.IProductoDAO;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;
import com.RutaDelSabor.ruta.models.entities.Producto;
import com.RutaDelSabor.ruta.models.entities.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unused")
@Service
public class PedidoServiceImpl implements IPedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);
    // ❌ ELIMINAR @Column aquí - no va en constantes
    private static final BigDecimal COSTO_DELIVERY_PREDETERMINADO = new BigDecimal("5.00");

    @Autowired private IPedidoDAO pedidoRepository;
    @Autowired private IProductoDAO productoRepository;
    @Autowired private IClienteDAO clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> GetAll() {
        log.info("Obteniendo todos los pedidos (solo para admin).");
        List<Pedido> list = new ArrayList<>();
        pedidoRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional
    public Pedido Save(Pedido object) {
        log.info("Guardando pedido ID: {}", object.getID() != null ? object.getID() : "NUEVO");
        object.setAudAnulado(false);
        return pedidoRepository.save(object);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido FindByID(Long id) {
        log.info("Buscando pedido por ID: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException("Pedido no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void Delete(Long id) {
        log.warn("Realizando borrado lógico de pedido ID: {}", id);
        Pedido pedido = FindByID(id);
        pedido.setAudAnulado(true);
        pedidoRepository.save(pedido);
        log.info("Pedido ID: {} marcado como anulado.", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pedido crearNuevaOrden(OrdenRequestDTO ordenRequest, UserDetails userDetails) {
        log.info("Iniciando creación de orden para usuario: {}", userDetails.getUsername());

        Cliente cliente = clienteRepository.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado: " + userDetails.getUsername()));
        log.debug("Cliente encontrado: ID {}", cliente.getID());

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setDireccion(ordenRequest.getDireccionEntrega());
        nuevoPedido.setReferencia(ordenRequest.getReferenciaEntrega());
        
        Estado estadoInicial = new Estado();
        estadoInicial.setTipo_Estado("RECIBIDO");
        nuevoPedido.addEstadoHistorial(estadoInicial);
        log.debug("Establecido estado inicial: RECIBIDO");
        
        // ❌ ELIMINAR @Column aquí - no va en variables locales
        BigDecimal subtotalCalculado = BigDecimal.ZERO;
        List<PedidoDetallado> detalles = new ArrayList<>();

        for (ItemDTO item : ordenRequest.getItems()) {
            Producto producto = productoRepository.findByIdAndLock(item.getProductoId())
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getProducto() + " (Disponibles: " + producto.getStock() + ")");
            }

            PedidoDetallado detalle = new PedidoDetallado();
            detalle.setPedido(nuevoPedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            
            // Multiplicar BigDecimal correctamente
            BigDecimal subtotalItem = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
            detalle.setSubtotal(subtotalItem);
            detalles.add(detalle);

            // Sumar BigDecimal correctamente
            subtotalCalculado = subtotalCalculado.add(subtotalItem);
            producto.setStock(producto.getStock() - item.getCantidad());
        }

        nuevoPedido.setDetalles(detalles);
        nuevoPedido.setSubtotal(subtotalCalculado);
        
        // Usar BigDecimal correctamente
        BigDecimal costoDelivery = "Delivery".equalsIgnoreCase(ordenRequest.getTipoEntrega()) 
            ? COSTO_DELIVERY_PREDETERMINADO 
            : BigDecimal.ZERO;
        nuevoPedido.setMonto_Agregado(costoDelivery);
        
        // Sumar BigDecimal correctamente
        nuevoPedido.setTotal(subtotalCalculado.add(costoDelivery));

        if ("Tarjeta".equalsIgnoreCase(ordenRequest.getMetodoPago())) {
            if(ordenRequest.getNumeroTarjeta() != null && ordenRequest.getNumeroTarjeta().length() >= 4){
               nuevoPedido.setUltimosDigitosTarjeta(ordenRequest.getNumeroTarjeta().substring(ordenRequest.getNumeroTarjeta().length() - 4));
            }
            nuevoPedido.setTitularTarjeta(ordenRequest.getTitularTarjeta());
        } else if ("Yape".equalsIgnoreCase(ordenRequest.getMetodoPago())) {
            nuevoPedido.setNumeroYape(ordenRequest.getNumeroYape());
        }

        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        log.info("Pedido creado exitosamente con ID: {}", pedidoGuardado.getID());

        return pedidoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public String obtenerEstadoPedido(Long pedidoId, UserDetails userDetails) {
        log.debug("Buscando estado del pedido ID: {} para usuario {}", pedidoId, userDetails.getUsername());
        Pedido pedido = FindByID(pedidoId);

        if (!pedido.getCliente().getCorreo().equals(userDetails.getUsername())) {
             log.warn("Intento de acceso no autorizado al pedido ID {} por usuario {}", pedidoId, userDetails.getUsername());
             throw new PedidoNoEncontradoException("Pedido no encontrado o no pertenece al usuario.");
        }
        
        return pedido.getEstadoActual() != null ? pedido.getEstadoActual() : "DESCONOCIDO";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerHistorialPedidos(UserDetails userDetails) {
        log.debug("Obteniendo historial de pedidos para usuario {}", userDetails.getUsername());
        Cliente cliente = clienteRepository.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado: " + userDetails.getUsername()));
        return pedidoRepository.findByCliente_IdOrderByFechaPedidoDesc(cliente.getID());
    }

    @Transactional
    public Pedido actualizarEstadoPedido(Long pedidoId, String nuevoEstadoStr, String notas) {
        log.info("Actualizando estado del pedido ID: {} a '{}'", pedidoId, nuevoEstadoStr);
        Pedido pedido = FindByID(pedidoId);

        Estado nuevoHistorial = new Estado();
        nuevoHistorial.setTipo_Estado(nuevoEstadoStr);
        nuevoHistorial.setNotas(notas);

        pedido.addEstadoHistorial(nuevoHistorial);

        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        log.info("Estado del pedido ID {} actualizado a '{}'", pedidoActualizado.getID(), pedidoActualizado.getEstadoActual());
        return pedidoActualizado;
    }
}