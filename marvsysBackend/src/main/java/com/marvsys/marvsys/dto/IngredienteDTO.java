package com.marvsys.marvsys.dto;

import java.io.Serializable;

import com.marvsys.marvsys.entities.Ingrediente;
import com.marvsys.marvsys.entities.enums.StatusEstoque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredienteDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Size(max = 40, message = "tamanho máximo de caracteres é 40. ")
	@NotBlank(message = "Campo Obrigatório. ")
	private String nome; 
	
	private int quantidade; 
	
	private StatusEstoque statusEstoque;
	
	public IngredienteDTO() {}
	
	public IngredienteDTO(Ingrediente entity) {
		id = entity.getId();
		nome = entity.getNome();
		quantidade = entity.getQuantidade();
		statusEstoque = entity.getStatusEstoque();
	}

}
