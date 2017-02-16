package br.com.cardapio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the PRODUTO_PERGUNTA database table.
 */
@Entity
@Table(name = "PRODUTO_PERGUNTA")
public class ProdutoPergunta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PRODUTO_PERGUNTA", unique = true, nullable = false)
	private Integer id;

	@Column(name = "FL_RESPOSTA_OBRIGATORIA")
	private Boolean flRespostaObrigatoria = false;

	// bi-directional many-to-one association to Pergunta
	@ManyToOne
	@JoinColumn(name = "ID_PERGUNTA")
	private Pergunta pergunta;

	// bi-directional many-to-one association to Produto
	@ManyToOne
	@JoinColumn(name = "ID_PRODUTO")
	private Produto produto;

	@Transient
	private String respostaCliente;

	public ProdutoPergunta(Pergunta pergunta, Produto produto) {
		super();
		this.pergunta = pergunta;
		this.produto = produto;
	}

	public ProdutoPergunta() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idProdutoPergunta) {
		this.id = idProdutoPergunta;
	}

	public Boolean getFlRespostaObrigatoria() {
		return this.flRespostaObrigatoria;
	}

	public void setFlRespostaObrigatoria(Boolean flRespostaObrigatoria) {
		this.flRespostaObrigatoria = flRespostaObrigatoria;
	}

	public Pergunta getPergunta() {
		return this.pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public String getRespostaCliente() {
		return respostaCliente;
	}

	public void setRespostaCliente(String respostaCliente) {
		this.respostaCliente = respostaCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((pergunta == null) ? 0 : pergunta.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoPergunta other = (ProdutoPergunta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pergunta == null) {
			if (other.pergunta != null)
				return false;
		} else if (!pergunta.equals(other.pergunta))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
}
