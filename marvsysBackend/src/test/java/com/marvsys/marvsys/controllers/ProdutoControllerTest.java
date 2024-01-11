package com.marvsys.marvsys.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.marvsys.marvsys.dto.ProdutoDTO;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.services.ProdutoService;

@SpringBootTest
public class ProdutoControllerTest {
	
	private static final StatusEstoque STATUS = StatusEstoque.DISPONIVEL;

	private static final int QUANTIDADE = 1;

	private static final String NOME = "Coca";

	private static final long ID = 1L;

	private ProdutoDTO produtoDTO;
	
	@Mock
	private ProdutoService service;
	
	@InjectMocks
	private ProdutoController controller;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startProduto();
	}

	@DisplayName("Deve retornar um ProdutoDTO.")
	@Test
	public void shouldReturnCreate() {
		when(service.create(any(ProdutoDTO.class))).thenReturn(produtoDTO);
		var response = controller.create(produtoDTO);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		assertEquals(STATUS, response.getBody().getStatusEstoque());
		verify(service).create(produtoDTO);
	}

	@DisplayName("Deve atualizar ProdutoDTO.")
	@Test
	public void shouldReturnUpdate() {
		when(service.update(produtoDTO, ID)).thenReturn(produtoDTO);
		var response = controller.update(produtoDTO, ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		assertEquals(STATUS, response.getBody().getStatusEstoque());
		verify(service).update(produtoDTO, ID);
	}

	@DisplayName("Deve retornar um Produto.")
	@Test
	public void shouldReturnFindByID() {
		when(service.findById(anyLong())).thenReturn(produtoDTO);
		var response = controller.findById(ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ID, response.getBody().getId());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		assertEquals(STATUS, response.getBody().getStatusEstoque());
		verify(service).findById(ID);
	}

	@DisplayName("Deve deletar um produto.")
	@Test
	public void shouldDeleteProduto() {
		doNothing().when(service).delete(anyLong());
		ResponseEntity<Void> response = controller.delete(ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		verify(service, times(1)).delete(anyLong());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	void whenFindAllThenReturnSuccess() {
		Pageable pageable = PageRequest.of(0, 10); 
		Page<ProdutoDTO> page = new PageImpl<>(Collections.emptyList(), pageable, 0); 
		when(service.findAllPaged(pageable)).thenReturn(page);

		ResponseEntity<Page<ProdutoDTO>> response = controller.findAllPaged(pageable);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(page, response.getBody());
		verify(service).findAllPaged(pageable);
	}

	private void startProduto() {
		produtoDTO = new ProdutoDTO(ID, NOME, QUANTIDADE, STATUS, null);
	}

}
