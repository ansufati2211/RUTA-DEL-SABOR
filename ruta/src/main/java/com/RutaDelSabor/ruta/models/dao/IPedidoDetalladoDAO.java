package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;

public interface IPedidoDetalladoDAO extends CrudRepository<PedidoDetallado, Long>{

    @Query("SELECT pd.producto.ID, pd.producto.Producto, SUM(pd.Cantidad) as totalVendido FROM PedidoDetallado pd WHERE pd.pedido.AudAnulado = false GROUP BY pd.producto.ID, pd.producto.Producto ORDER BY totalVendido DESC")
List<Object[]> findTopVentas(Pageable pageable);

}
