package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the MESA database table.
 */
@Entity
@Table(name = "PLANTA_SALAO")
public class PlantaSalao implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Character STATUS_ATIVO = 'A';
	public static final Character STATUS_INATIVO = 'I';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PLANTA_SALAO", unique = true, nullable = false)
	private Integer id;

	@Column(name = "NM_PLANTA_SALAO")
	private String nome;

	@Column(name = "QT_LARGURA")
	private Integer larguraCm;

	@Column(name = "QT_COMPRIMENTO")
	private Integer comprimentoCm;

	@Column(name = "IN_STATUS")
	private Character status = STATUS_ATIVO;

	@ManyToOne
	@JoinColumn(name = "ID_EMPRESA")
	private Empresa empresa;

	public PlantaSalao() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idMesa) {
		this.id = idMesa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Integer getLarguraCm() {
		return larguraCm;
	}

	public void setLarguraCm(Integer larguraCm) {
		this.larguraCm = larguraCm;
	}

	public Integer getComprimentoCm() {
		return comprimentoCm;
	}

	public void setComprimentoCm(Integer comprimentoCm) {
		this.comprimentoCm = comprimentoCm;
	}


	
}
