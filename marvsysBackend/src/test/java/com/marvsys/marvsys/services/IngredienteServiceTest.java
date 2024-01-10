package com.marvsys.marvsys.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.marvsys.marvsys.dto.IngredienteDTO;
import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.repositories.IngredienteRepository;
import com.marvsys.marvsys.services.exceptions.DataIntegratyViolationException;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

@SpringBootTest
public class IngredienteServiceTest {

	private static final long ID = 1L;

	private static final String NOME = "copo";

	private static final int QUANTIDADE = 15;

	private static final StatusEstoque STATUS = StatusEstoque.DISPONIVEL;

	private Ingrediente ingrediente;

	private IngredienteDTO ingredienteDTO;

	private Optional<Ingrediente> optionalIngrediente;

	@InjectMocks
	private IngredienteService service;

	@Mock
	private IngredienteRepository repository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		startIngrediente();
	}

	@DisplayName("Deve retornar um Ingrediente com sucesso.")
	@Test
	public void shouldReturnAIngredienteWithSuccess() {
		when(repository.findById(anyLong())).thenReturn(optionalIngrediente);
		var response = service.findById(ID);
		assertNotNull(response);
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
		assertEquals(STATUS, response.getStatusEstoque());
	}

	@DisplayName("Deve retornar Ingrediente não encontrado.")
	@Test
	public void shouldReturnFindByIdIngredienteNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.findById(ID);
		});
		assertEquals("ID do Ingrediente informado não encontrado. ", response.getMessage());
	}

	@DisplayName("Deve alterar o ingrediente com sucesso quando encontrado.")
	@Test
	void whenUpdateIngredienteThenReturnIngredienteDTO() {
		when(repository.findById(anyLong())).thenReturn(optionalIngrediente);
		var response = service.updateIngrediente(ingredienteDTO, ID);
		assertNotNull(response);
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(QUANTIDADE, response.getQuantidade());
	}

	@DisplayName("Atualização Deve retornar Ingrediente não encontrado.")
	@Test
	public void shouldUpdateReturnIngredienteNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		ObjectNotFoundException response = assertThrows(ObjectNotFoundException.class, () -> {
			service.updateIngrediente(ingredienteDTO, ID);
		});
		assertEquals("ID do ingrediente informado não encontrado. ", response.getMessage());

	}

	@DisplayName("Deve criar um ingrediente.")
	@Test
	public void shouldCreateIngredienteDTO() {
		when(repository.save(any(Ingrediente.class))).thenReturn(ingrediente);
		var response = service.create(ingredienteDTO);
		assertNotNull(response);
		assertEquals(NOME, response.getNome());
		assertEquals(STATUS, response.getStatusEstoque());
		assertEquals(QUANTIDADE, response.getQuantidade());
		verify(repository, times(1)).save(any(Ingrediente.class));
	}

	@DisplayName("Deve excluir um ingrediente.")
	@Test
	public void shouldDeleteIngrediente() {
		Long ingredienteId = ID;
		when(repository.findById(ingredienteId)).thenReturn(optionalIngrediente);
		service.delete(ingredienteId);
		verify(repository).findById(ingredienteId);
		verify(repository).deleteById(ingredienteId);
	}

	@DisplayName("Deveria retornar Não pode deletar um ingrediente associado.")
	@Test
	public void shouldNotDeleteAssociatedIngredient() {
		Long id = ID;
		when(repository.findById(id)).thenReturn(Optional.of(ingrediente));
		doThrow(DataIntegratyViolationException.class).when(repository).deleteById(ID);
		assertThrows(DataIntegratyViolationException.class, () -> service.delete(ID));

	}

	private void startIngrediente() {
		ingrediente = new Ingrediente(ID, NOME, QUANTIDADE, STATUS);
		ingredienteDTO = new IngredienteDTO(ID, NOME, QUANTIDADE, STATUS);
		optionalIngrediente = Optional.of(ingrediente);
	}

}
