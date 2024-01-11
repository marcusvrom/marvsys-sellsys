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

import com.marvsys.marvsys.dto.ProdutoDTO;
import com.marvsys.marvsys.services.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Marv SYS endpoint produto")
@RestController
@RequestMapping(value = "/produto")
public class ProdutoController {

	@Autowired
	private ProdutoService service;
	
	@Operation(summary = "Busca um produto na base de dados")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProdutoDTO> findById(@PathVariable Long id) {
		var response = service.findById(id);
		return ResponseEntity.ok().body(response);
	}
		
	@Operation(summary = "trás uma lista com paginação de produtos")
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findAllPaged(Pageable pageable){
		Page<ProdutoDTO> response = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Cria um produto.")
	@PostMapping
	public ResponseEntity<ProdutoDTO> create(@Valid @RequestBody ProdutoDTO dto) {
		var newObj = service.create(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).body(newObj);
	}
	
	@Operation(summary = "Atualiza dados do produtos.")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<ProdutoDTO> update(@RequestBody ProdutoDTO dto, @PathVariable Long id) {
		var response = service.update(dto, id);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Deleta um produto pelo ID.")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
