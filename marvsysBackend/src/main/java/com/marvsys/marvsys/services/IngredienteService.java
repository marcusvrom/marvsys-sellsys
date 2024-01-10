package com.marvsys.marvsys.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvsys.marvsys.dto.IngredienteDTO;
import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.repositories.IngredienteRepository;
import com.marvsys.marvsys.services.exceptions.DataIntegratyViolationException;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository repository;
	
	public IngredienteDTO findById(Long id) {
		Optional<Ingrediente> ingreOptional = repository.findById(id);
		Ingrediente entity = ingreOptional
				.orElseThrow(()-> new ObjectNotFoundException("ID do Ingrediente informado não encontrado. "));
		return new IngredienteDTO(entity);
	}
	
	@Transactional
	public IngredienteDTO create(IngredienteDTO objDto) {
		Ingrediente entity = new Ingrediente();
		entity.setNome(objDto.getNome());
		entity.setQuantidade(objDto.getQuantidade());
		entity.setStatusEstoque(objDto.getStatusEstoque());
		repository.save(entity);
		return new IngredienteDTO(entity);
	}
	
	@Transactional
	public IngredienteDTO updateIngrediente(IngredienteDTO objDto, Long id) {
		Ingrediente ingrediente = repository.findById(id)
				.orElseThrow(()-> new ObjectNotFoundException("ID do ingrediente informado não encontrado. "));
		ingrediente.setNome(objDto.getNome());
		ingrediente.setQuantidade(objDto.getQuantidade());
		ingrediente.setStatusEstoque(objDto.getStatusEstoque());
		repository.save(ingrediente);
		return new IngredienteDTO(ingrediente);
		
	}
	
	@Transactional
	public void delete(Long id) {
		findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegratyViolationException e) {
			throw new DataIntegratyViolationException("Não pode deletar ingredientes associados com um produto. ");
		}
		
	}

}
