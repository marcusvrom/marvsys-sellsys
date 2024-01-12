package com.marvsys.marvsys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.marvsys.marvsys.entities.Combo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComboDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(max = 40, message = "tamanho máximo de caracteres é 40. ")
	@NotBlank(message = "Campo Obrigatório. ")
	private String nome;
	private int quantidade;
	private Set<ProdutoDTO> produtos = new HashSet<>();

	public ComboDTO(Combo entity) {
		id = entity.getId();
		quantidade = entity.getQuantidade();
		nome = entity.getNome();
		entity.getProdutos().forEach(x -> this.produtos.add(new ProdutoDTO(x)));
	}

}
