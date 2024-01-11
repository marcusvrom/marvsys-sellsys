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

import com.marvsys.marvsys.dto.IngredienteInsertDTO;
import com.marvsys.marvsys.dto.ProdutoDTO;
import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.entities.Produto;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.repositories.IngredienteRepository;
import com.marvsys.marvsys.repositories.ProdutoRepository;
import com.marvsys.marvsys.services.exceptions.DataIntegratyViolationException;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

@SpringBootTest
public class ProdutoServiceTest {
	
	private static final String INGREDIENTE = "gelo";
	private static final StatusEstoque STATUS = StatusEstoque.DISPONIVEL;
	private static final int QUANTIDADE = 1;
	private static final String NOME = "Coca";
	private static final long ID = 1L;
	private ProdutoDTO produtoDTO;
	private Produto produto;
	private Optional<Produto> optionalProtudo;
	
	private IngredienteInsertDTO ingredienteInsertDTO;
	private Ingrediente ingrediente;
	
	@InjectMocks
	private ProdutoService service;
	
	@Mock
	private ProdutoRepository repository;
	
	@Mock
	private IngredienteRepository ingredienteRepository;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startProduto();
	}
	
	@DisplayName("Deveria retornar uma paginação de produtos.")
	@Test 
	void whenFindAllPagedThenReturnPageOfProdutoDTO() {
		List<Produto> produtos = Arrays.asList(new Produto(2L, "pro1", 2, StatusEstoque.DISPONIVEL),
				new Produto(ID, NOME, QUANTIDADE, STATUS));
		Page<Produto> proPage = new PageImpl<>(produtos);
		when(repository.findAll(any(Pageable.class))).thenReturn(proPage);
		Page<ProdutoDTO> response = service.findAllPaged(PageRequest.of(0, 5));
		assertNotNull(response);
	}
	
	@DisplayName("Deveria retornar uma paginação vazia.")
	@Test 
	void whenFindAllPagedThenReturnEmptyPage() {
		Page<Produto> proPage = new PageImpl<>(Collections.emptyList());
		when(repository.findAll(any(Pageable.class))).thenReturn(proPage);
		Page<ProdutoDTO> result = service.findAllPaged(PageRequest.of(0, 10));
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
	
	@DisplayName("Deve excluir um Produto.")
	@Test
	public void shouldDeleteProduto() {
		Long protudoId = ID;
		when(repository.findById(protudoId)).thenReturn(optionalProtudo);
		service.delete(protudoId);
		verify(repository).findById(protudoId);
		verify(repository).deleteById(protudoId);
	}

	@DisplayName("Deve alterar o produto com sucesso quando encontrado.")
	@Test
	void whenUpdateIngredienteThenReturnProdutoDTO() {
		when(repository.findById(anyLong())).thenReturn(optionalProtudo);
		var response = service.update(produtoDTO, ID);
		assertNotNull(response);
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
	}

	@DisplayName("Atualização Deve retornar produto não encontrado.")
	@Test
	public void shouldUpdateReturnProdutoNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.update(produtoDTO, ID);
		});
		assertEquals("Produto não encontrado. ", response.getMessage());

	}
	
	@DisplayName("Deve retornar um Produto com sucesso.")
	@Test
	public void shouldReturnAProdutoWithSuccess() {
		when(repository.findById(anyLong())).thenReturn(optionalProtudo);
		var response = service.findById(ID);
		assertNotNull(response);
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
		assertEquals(STATUS, response.getStatusEstoque());
	}

	@DisplayName("Deve retornar Produto não encontrado.")
	@Test
	public void shouldReturnFindByIdProdutoNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.findById(ID);
		});
		assertEquals("ID do produto informado não encontrado. ", response.getMessage());
	}
	
	@DisplayName("Deve retorna ingrediente não encontrado")
	@Test
	public void shouldReturnIngredienteNotFound() {
		when(ingredienteRepository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.create(produtoDTO);
		});
		assertEquals("ID do ingrediente informado não encontrado. ", response.getMessage());
	}
	
	@DisplayName("Deve retorna DataIntegratyViolationException")
	@Test
	public void shouldReturnDataIngrException() {
		when(ingredienteRepository.findById(anyLong())).thenReturn(Optional.of(ingrediente));
		produtoDTO.setQuantidade(10); 	    
		ingrediente.setQuantidade(5);
		DataIntegratyViolationException exception = assertThrows(DataIntegratyViolationException.class, () -> {
	        service.create(produtoDTO);
	    });
	    assertEquals("Quantidade insuficiente do ingrediente " + ingrediente.getId(), exception.getMessage());
	}
	
	@DisplayName("Deve crair um produto")
	@Test
	public void shouldCreateProdutoDTO() {
		when(ingredienteRepository.findById(anyLong())).thenReturn(Optional.of(ingrediente));
		when(repository.save(any(Produto.class))).thenReturn(produto);
		ProdutoDTO response = service.create(produtoDTO);
		assertNotNull(response);
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
		assertEquals(STATUS, response.getStatusEstoque());
		verify(repository, times(1)).save(any(Produto.class));
	}
	
	private void startProduto() {
		produto = new Produto(ID, NOME, QUANTIDADE, STATUS);
		ingrediente = new Ingrediente(ID, INGREDIENTE, QUANTIDADE, STATUS);
		optionalProtudo = Optional.of(produto);
		ingredienteInsertDTO = new IngredienteInsertDTO(ID, QUANTIDADE, INGREDIENTE);
		produtoDTO = new ProdutoDTO(ID, NOME, QUANTIDADE, STATUS, new HashSet<>());
		produtoDTO.getIngredientes().add(ingredienteInsertDTO);		
	}

}
