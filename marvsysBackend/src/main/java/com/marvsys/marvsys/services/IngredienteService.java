package com.marvsys.marvsys.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvsys.marvsys.dto.IngredienteDTO;
import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.repositories.IngredienteRepository;

import jakarta.transaction.Transactional;

@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository repository;
	
	public IngredienteDTO findById(Long id) {
		Optional<Ingrediente> ingreOptional = repository.findById(id);
		Ingrediente entity = ingreOptional.orElse(null);
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
		Ingrediente ingrediente = repository.findById(id).orElse(null);
		ingrediente.setNome(objDto.getNome());
		ingrediente.setQuantidade(objDto.getQuantidade());
		ingrediente.setStatusEstoque(objDto.getStatusEstoque());
		repository.save(ingrediente);
		return new IngredienteDTO(ingrediente);
		
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

}
