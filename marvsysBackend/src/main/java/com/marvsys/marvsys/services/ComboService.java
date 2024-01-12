package com.marvsys.marvsys.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marvsys.marvsys.dto.ComboDTO;
import com.marvsys.marvsys.entities.Combo;
import com.marvsys.marvsys.repositories.ComboRepository;
import com.marvsys.marvsys.repositories.ProdutoRepository;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ComboService {

	@Autowired
	private ComboRepository repository;

	@Autowired
	private ProdutoRepository produtoRepository;

	public ComboDTO findById(Long id) {
		Optional<Combo> co = repository.findById(id);
		Combo entity = co.orElseThrow(() -> new ObjectNotFoundException("Combo informado não encontrado. "));
		return new ComboDTO(entity);
	}
	
	@Transactional
	public ComboDTO update(ComboDTO comboDTO, Long id) {
		Combo combo = repository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Combo informado não encontrado. "));
		combo.setNome(comboDTO.getNome());
		combo.setQuantidade(comboDTO.getQuantidade());
		repository.save(combo);
		return new ComboDTO(combo);
	}

	public Page<ComboDTO> findAllPaged(Pageable pageable) {
		Page<Combo> combos = repository.findAll(pageable);
		return combos.map(x -> new ComboDTO(x));
	}

	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}
	
	@Transactional
	public ComboDTO create(ComboDTO comboDTO) {
		Combo combo = new Combo();
		combo.setNome(comboDTO.getNome());
		combo.setQuantidade(comboDTO.getQuantidade());
		combo.getProdutos()
				.addAll(comboDTO.getProdutos().stream()
						.map(produtoDTO -> produtoRepository.findById(produtoDTO.getId())
								.orElseThrow(() -> new ObjectNotFoundException("Produto informado não encontrado. ")))
						.collect(Collectors.toList()));
		repository.save(combo);
		return new ComboDTO(combo);
	}
}
