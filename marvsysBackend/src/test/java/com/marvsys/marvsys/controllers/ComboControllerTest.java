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

import com.marvsys.marvsys.dto.ComboDTO;
import com.marvsys.marvsys.services.ComboService;

@SpringBootTest
public class ComboControllerTest {
	
	private static final int QUANTIDADE = 1;

	private static final String NOME = "Coca";

	private static final long ID = 1L;

	private ComboDTO comboDTO;

	@Mock
	private ComboService service;

	@InjectMocks
	private ComboController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startProduto();
	}

	@DisplayName("Deve retornar um ComboDTO.")
	@Test
	public void shouldReturnCreate() {
		when(service.create(any(ComboDTO.class))).thenReturn(comboDTO);
		var response = controller.create(comboDTO);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		verify(service).create(comboDTO);
	}

	@DisplayName("Deve atualizar ComboDTO.")
	@Test
	public void shouldReturnUpdate() {
		when(service.update(comboDTO, ID)).thenReturn(comboDTO);
		var response = controller.update(comboDTO, ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		verify(service).update(comboDTO, ID);
	}

	@DisplayName("Deve retornar um Produto.")
	@Test
	public void shouldReturnFindByID() {
		when(service.findById(anyLong())).thenReturn(comboDTO);
		var response = controller.findById(ID);
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ID, response.getBody().getId());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(QUANTIDADE, response.getBody().getQuantidade());
		verify(service).findById(ID);
	}

	@DisplayName("Deve deletar um ComboDTO.")
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
		Page<ComboDTO> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
		when(service.findAllPaged(pageable)).thenReturn(page);

		ResponseEntity<Page<ComboDTO>> response = controller.findAllPaged(pageable);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(page, response.getBody());
		verify(service).findAllPaged(pageable);
	}

	private void startProduto() {
		comboDTO = new ComboDTO(ID, NOME, QUANTIDADE, null);
	}

}
