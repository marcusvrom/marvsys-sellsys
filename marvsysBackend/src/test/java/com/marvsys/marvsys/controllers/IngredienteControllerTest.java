package com.marvsys.marvsys.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.marvsys.marvsys.dto.IngredienteDTO;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.services.IngredienteService;

@SpringBootTest
public class IngredienteControllerTest {

	private static final long ID = 1L;

	private static final String NOME = "copo";

	private static final int QUANTIDADE = 15;

	private static final StatusEstoque STATUS = StatusEstoque.DISPONIVEL;

	private IngredienteDTO ingredienteDTO;

	@Mock
	private IngredienteService service;

	@InjectMocks
	private IngredienteController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startIngrediente();
	}

	@DisplayName("Deve retornar um Ingrediente.")
	@Test
	public void shouldReturnCreate() {
		when(service.create(any(IngredienteDTO.class))).thenReturn(ingredienteDTO);
		var response = controller.create(ingredienteDTO);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		assertEquals(STATUS, response.getBody().getStatusEstoque());
		verify(service).create(ingredienteDTO);
	}

	@DisplayName("Deve atualizar Ingrediente.")
	@Test
	public void shouldReturnUpdate() {
		when(service.updateIngrediente(ingredienteDTO, ID)).thenReturn(ingredienteDTO);
		var response = controller.update(ingredienteDTO, ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		assertEquals(STATUS, response.getBody().getStatusEstoque());
		verify(service).updateIngrediente(ingredienteDTO, ID);
	}

	@DisplayName("Deve retornar um Ingrediente.")
	@Test
	public void shouldReturnFindByID() {
		when(service.findById(anyLong())).thenReturn(ingredienteDTO);
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

	@DisplayName("Deve deletar um ingrediente.")
	@Test
	public void shouldDeleteIngrediente() {
		doNothing().when(service).delete(anyLong());
		ResponseEntity<Void> response = controller.delete(ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		verify(service, times(1)).delete(anyLong());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	private void startIngrediente() {
		ingredienteDTO = new IngredienteDTO(ID, NOME, QUANTIDADE, STATUS);
	}

}
