package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.RutaDelSabor.ruta.models.entities.Categoria;
import java.util.List;

// Renombrar a CategoriaRepository sería mejor práctica
public interface ICategoriaDAO extends CrudRepository<Categoria, Long> {
    @Query("SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.productos p WHERE c.AudAnulado = false AND p.AudAnulado = false ORDER BY c.Categoria") // Modificado para excluir anulados y ordenar
    List<Categoria> findAllWithProducts();

     // Sobrescribir findAll para excluir anulados por defecto (opcional)
     @Query("SELECT c FROM Categoria c WHERE c.AudAnulado = false ORDER BY c.Categoria")
     List<Categoria> findAllActive();
}