package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Categoria;
import com.RutaDelSabor.ruta.services.ICategoriaService;


@RestController
@RequestMapping("/api")


public class CategoriaController {

    @Autowired
    private ICategoriaService m_Service;


    @GetMapping("/categorias")
	public List<Categoria> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/categoria")
	@ResponseStatus(HttpStatus.CREATED)
	public Categoria saveAbono(@RequestBody Categoria categoria) {
		return m_Service.Save(categoria);
	}

	@GetMapping("/categoria/{id}")
	public Categoria getCategoriaByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/categoria/{id}")
	public Categoria updateCategoria(@RequestBody Categoria categoria, @PathVariable Long id) {
		categoria.setID(id);
		return m_Service.Save(categoria);
	}

	@DeleteMapping("/categoria/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategoria(@PathVariable Long id) {
		Categoria entity = m_Service.FindByID(id);
		entity.setAudAnulado(true);
		m_Service.Save(entity);
	}
}
