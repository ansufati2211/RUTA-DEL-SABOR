package com.RutaDelSabor.ruta.models.dao;

 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;

import com.RutaDelSabor.ruta.models.entities.Pedido;
 import java.util.List;

 public interface IPedidoDAO extends CrudRepository<Pedido, Long> {

     // Para obtener historial de un cliente
     List<Pedido> findByCliente_IdOrderByFechaPedidoDesc(Long clienteId);

     // Para obtener pedidos activos para admin/vendedor (ejemplo)
     // Necesitarías una forma de saber el estado actual (ej. campo en Pedido o relación con Estado)
     // @Query("SELECT p FROM Pedido p WHERE p.estadoActual = 'RECIBIDO' OR p.estadoActual = 'EN_PREPARACION'")
     // List<Pedido> findPedidosActivos();
    @Query("SELECT SUM(p.Total) FROM Pedido p WHERE p.Fecha_Pedido BETWEEN :inicio AND :fin AND p.AudAnulado = false")
Double sumTotalBetweenDates(@Param("inicio") Date inicio, @Param("fin") Date fin);

@Query("SELECT COUNT(p) FROM Pedido p WHERE p.Fecha_Pedido BETWEEN :inicio AND :fin AND p.AudAnulado = false")
Long countBetweenDates(@Param("inicio") Date inicio, @Param("fin") Date fin);
 }