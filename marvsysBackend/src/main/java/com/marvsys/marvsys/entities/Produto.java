package com.marvsys.marvsys.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.marvsys.marvsys.entities.enums.StatusEstoque;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tb_produto")
public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private int quantidade;

	private StatusEstoque statusEstoque;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_produto_ingrediente", joinColumns = @JoinColumn(name = "produto_id"), inverseJoinColumns = @JoinColumn(name = "ingrediente_id"))
	private Set<Ingrediente> ingredientes = new HashSet<>();

	public Produto(Long id, String nome, int quantidade, StatusEstoque statusEstoque) {
		super();
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.statusEstoque = statusEstoque;
	}

}
