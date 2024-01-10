package com.marvsys.marvsys.dto;

import java.io.Serializable;

import com.marvsys.marvsys.entities.Ingrediente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class IngredienteInsertDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
		
	private int quantidade;
	
	private String nome;
		
	public IngredienteInsertDTO() {}
	
	public IngredienteInsertDTO(Ingrediente entity) {
		id = entity.getId();
		quantidade = entity.getQuantidade();
		nome = entity.getNome();
	}

}
