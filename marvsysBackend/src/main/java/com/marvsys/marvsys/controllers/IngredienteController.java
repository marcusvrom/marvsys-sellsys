package com.marvsys.marvsys.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marvsys.marvsys.dto.IngredienteDTO;
import com.marvsys.marvsys.services.IngredienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Marv SYS endpoint")
@RestController
@RequestMapping(value = "/ingrediente")
public class IngredienteController {

	@Autowired
	private IngredienteService service;
	
	@Operation(summary = "Busca um ingrediente pelo ID.")
	@GetMapping(value = "/{id}")
	public ResponseEntity<IngredienteDTO> findById(@PathVariable Long id) {
		var response = service.findById(id);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Cria um ingrediente no banco de dados.")
	@PostMapping
	public ResponseEntity<IngredienteDTO> create(@Valid @RequestBody IngredienteDTO dto){
		var newObj = service.create(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).body(newObj);
	}
	
	@Operation(summary = "Atualiza um ingrediente no banco de dados.")
	@PutMapping(value = "/{id}")
	public ResponseEntity<IngredienteDTO> update(@Valid @RequestBody IngredienteDTO dto, @PathVariable Long id){
		var newObj = service.updateIngrediente(dto, id);
		return ResponseEntity.ok().body(newObj);
	}
	
	@Operation(summary = "Deleta um ingrediente não associado no banco de dados.")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	

}
