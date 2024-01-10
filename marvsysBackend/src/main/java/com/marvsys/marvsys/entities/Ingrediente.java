package com.marvsys.marvsys.entities;

import java.io.Serializable;

import com.marvsys.marvsys.entities.enums.StatusEstoque;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
@Getter
@Entity
@Table(name = "tb_ingrediente")
public class Ingrediente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private int quantidade;

	private StatusEstoque statusEstoque;

	public Ingrediente(Long id, String nome, int quantidade, StatusEstoque statusEstoque) {
		super();
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.statusEstoque = statusEstoque;
	}

}
