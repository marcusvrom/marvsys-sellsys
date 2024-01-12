package com.marvsys.marvsys.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

import com.marvsys.marvsys.dto.ComboDTO;
import com.marvsys.marvsys.dto.ProdutoDTO;
import com.marvsys.marvsys.entities.Combo;
import com.marvsys.marvsys.entities.Produto;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.repositories.ComboRepository;
import com.marvsys.marvsys.repositories.ProdutoRepository;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

@SpringBootTest
public class ComboServiceTest {

	private static final StatusEstoque STATUS = StatusEstoque.DISPONIVEL;
	private static final int QUANTIDADE = 1;
	private static final String NOME = "Coca";
	private static final long ID = 1L;

	private ProdutoDTO produtoDTO;
	private Produto produto;
	private Optional<Produto> optionalProtudo;

	private Combo combo;
	private ComboDTO comboDTO;
	private Optional<Combo> optionalCombo;

	@InjectMocks
	private ComboService service;

	@Mock
	private ProdutoRepository produtoRepository;

	@Mock
	private ComboRepository repository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startCombo();
	}

	@DisplayName("Deveria retornar uma paginação de combos.")
	@Test
	void whenFindAllPagedThenReturnPageOfComboDTO() {
		List<Combo> produtos = Arrays.asList(new Combo(2L, "pro1", 2),
				new Combo(ID, NOME, QUANTIDADE));
		Page<Combo> proPage = new PageImpl<>(produtos);
		when(repository.findAll(any(Pageable.class))).thenReturn(proPage);
		Page<ComboDTO> response = service.findAllPaged(PageRequest.of(0, 5));
		assertNotNull(response);
	}

	@DisplayName("Deveria retornar uma paginação vazia.")
	@Test
	void whenFindAllPagedThenReturnEmptyPage() {
		Page<Combo> comPage = new PageImpl<>(Collections.emptyList());
		when(repository.findAll(any(Pageable.class))).thenReturn(comPage);
		Page<ComboDTO> result = service.findAllPaged(PageRequest.of(0, 10));
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@DisplayName("Deve excluir um Combo.")
	@Test
	public void shouldDeleteCombo() {
		Long comboId = ID;
		when(repository.findById(comboId)).thenReturn(optionalCombo);
		service.delete(comboId);
		verify(repository).findById(comboId);
		verify(repository).deleteById(comboId);
	}

	@DisplayName("Deve alterar o Combo com sucesso quando encontrado.")
	@Test
	void whenUpdateIngredienteThenReturnComboDTO() {
		when(repository.findById(anyLong())).thenReturn(optionalCombo);
		var response = service.update(comboDTO, ID);
		assertNotNull(response);
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
	}

	@DisplayName("Atualização Deve retornar combo não encontrado.")
	@Test
	public void shouldUpdateReturnProdutoNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.update(comboDTO, ID);
		});
		assertEquals("Combo informado não encontrado. ", response.getMessage());
	}

	@DisplayName("Deve retornar um Combo com sucesso.")
	@Test
	public void shouldReturnAComboWithSuccess() {
		when(repository.findById(anyLong())).thenReturn(optionalCombo);
		var response = service.findById(ID);
		assertNotNull(response);
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
	}

	@DisplayName("Deve retornar Combo não encontrado.")
	@Test
	public void shouldReturnFindByIdComboNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.findById(ID);
		});
		assertEquals("Combo informado não encontrado. ", response.getMessage());
	}

	@DisplayName("Deve retorna produto não encontrado")
	@Test
	public void shouldReturnProdutoNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.create(comboDTO);
		});
		assertEquals("Produto informado não encontrado. ", response.getMessage());
	}

	@DisplayName("Deve crair um combo")
	@Test
	public void shouldCreateComboDTO() {
		when(produtoRepository.findById(anyLong())).thenReturn(optionalProtudo);
		when(repository.save(any(Combo.class))).thenReturn(combo);
		var response = service.create(comboDTO);
		assertNotNull(response);
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
		verify(repository, times(1)).save(any(Combo.class));
	}

	private void startCombo() {
		produto = new Produto(ID, NOME, QUANTIDADE, STATUS);
		optionalProtudo = Optional.of(produto);
		produtoDTO = new ProdutoDTO(ID, NOME, QUANTIDADE, STATUS, null);
		combo = new Combo(ID, NOME, QUANTIDADE);
		comboDTO = new ComboDTO(ID, NOME, QUANTIDADE, new HashSet<>());
		comboDTO.getProdutos().add(produtoDTO);
		optionalCombo = Optional.of(combo);

	}

}
