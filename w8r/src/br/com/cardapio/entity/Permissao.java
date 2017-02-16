package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PERMISSAO_CLIENTE_MESA")
public class Permissao implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISSAO", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "ID_MESA")
    private Mesa mesa;
	
	@ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;
	
	@Column(name="FL_APROVADO")
	private boolean flAprovado;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_CRIACAO")
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean isFlAprovado() {
		return flAprovado;
	}

	public void setFlAprovado(boolean flAprovado) {
		this.flAprovado = flAprovado;
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}
	
}
