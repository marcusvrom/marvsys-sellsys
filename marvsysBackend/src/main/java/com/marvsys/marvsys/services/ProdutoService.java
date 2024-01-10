package com.marvsys.marvsys.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marvsys.marvsys.dto.IngredienteInsertDTO;
import com.marvsys.marvsys.dto.ProdutoDTO;
import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.entities.Produto;
import com.marvsys.marvsys.entities.enums.StatusEstoque;
import com.marvsys.marvsys.repositories.IngredienteRepository;
import com.marvsys.marvsys.repositories.ProdutoRepository;
import com.marvsys.marvsys.services.exceptions.DataIntegratyViolationException;
import com.marvsys.marvsys.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	@Autowired
	private IngredienteRepository ingredienteRepository;

	public ProdutoDTO findById(Long id) {
		Optional<Produto> pro = repository.findById(id);
		Produto entity = pro.orElseThrow(() -> new ObjectNotFoundException("ID do produto informado não encontrado. "));
		return new ProdutoDTO(entity);
	}

	@Transactional
	public ProdutoDTO create(ProdutoDTO produtoDTO) {
		Produto produto = new Produto();
		produto.getIngredientes().clear();
		for(IngredienteInsertDTO ingredienteInsertDTO : produtoDTO.getIngredientes()) {
			Ingrediente ingrediente = ingredienteRepository.findById(ingredienteInsertDTO.getId())
					.orElseThrow(() -> new ObjectNotFoundException("ID do ingrediente informado não encontrado. "));
			produto.setQuantidade(produtoDTO.getQuantidade());
			int estoqueAtual = ingrediente.getQuantidade();
			int quantidadeUtilizada = produtoDTO.getQuantidade();
			if (estoqueAtual < quantidadeUtilizada) {
				throw new DataIntegratyViolationException("Quantidade insuficiente do ingrediente " + ingrediente.getId());
			}
			ingrediente.setQuantidade(estoqueAtual - quantidadeUtilizada);
			ingredienteRepository.save(ingrediente);
			produto.getIngredientes().add(ingrediente);
		}
		produto.setNome(produtoDTO.getNome());
		produto.setStatusEstoque(StatusEstoque.DISPONIVEL);
		repository.save(produto);
		return new ProdutoDTO(produto);
	}
	
	@Transactional
	public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado. "));
		produto.setNome(produtoDTO.getNome());
		produto.setStatusEstoque(produtoDTO.getStatusEstoque());
		repository.save(produto);
		return new ProdutoDTO(produto);
	}
	
	@Transactional
	public Page<ProdutoDTO> findAllPaged(Pageable pageable){
		Page<Produto> produto = repository.findAll(pageable);
		return produto.map(x -> new ProdutoDTO(x));
	}
	
	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}

}
