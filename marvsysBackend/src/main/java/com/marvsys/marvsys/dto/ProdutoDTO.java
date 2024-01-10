package com.marvsys.marvsys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.marvsys.marvsys.entities.Produto;
import com.marvsys.marvsys.entities.enums.StatusEstoque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Size(max = 40, message = "tamanho máximo de caracteres é 40. ")
	@NotBlank(message = "Campo Obrigatório. ")
	private String nome;
	
	private int quantidade;
	private StatusEstoque statusEstoque;
	private Set<IngredienteInsertDTO> ingredientes = new HashSet<>();
	
	public ProdutoDTO(Produto entity) {
		id = entity.getId();
		nome = entity.getNome();
		quantidade = entity.getQuantidade();
		statusEstoque = entity.getStatusEstoque();
		entity.getIngredientes().forEach(x -> this.ingredientes.add(new IngredienteInsertDTO(x)));
	}
	
	
	
	
}
