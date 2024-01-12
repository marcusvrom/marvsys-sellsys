package com.marvsys.marvsys.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marvsys.marvsys.dto.ComboDTO;
import com.marvsys.marvsys.services.ComboService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Marv SYS endpoint combos")
@RestController
@RequestMapping(value = "/combo")
public class ComboController {
	
	@Autowired
	private ComboService service;
	
	@Operation(summary = "Busca um combo.")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ComboDTO> findById(@PathVariable Long id) {
		var response = service.findById(id);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Busca combos com paginação.")
	@GetMapping
	public ResponseEntity<Page<ComboDTO>> findAllPaged(Pageable pageable){
		Page<ComboDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Atualiza um combo.")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<ComboDTO> update(@Valid @RequestBody ComboDTO  obj,@PathVariable Long id){
		var response = service.update(obj, id);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Cria um combo.")
	@PostMapping
	public ResponseEntity<ComboDTO> create(@Valid @RequestBody ComboDTO dto){
		var newObj = service.crete(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).body(newObj);
	}
	
	@Operation(summary = "Delete um combo.")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
