package com.marvsys.marvsys.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import lombok.Setter;

@EqualsAndHashCode(of = "id")
@Setter
@Getter
@Entity
@Table(name = "tb_combo")
public class Combo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private int quantidade;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_combo_produto", joinColumns = @JoinColumn(name = "combo_id"), inverseJoinColumns = @JoinColumn(name = "produto_id"))
	private Set<Produto> produtos = new HashSet<>();

	public Combo(Long id, String nome, int quantidade) {
		super();
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
	}

}
