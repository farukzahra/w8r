package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the RESPOSTA database table.
 * 
 */
@Entity
@Table(name = "RESPOSTA")
public class Resposta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_RESPOSTA", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DS_RESPOSTA")
	private String descricao;

	// bi-directional many-to-one association to Pergunta
	@ManyToOne
	@JoinColumn(name = "ID_PERGUNTA")
	private Pergunta pergunta;

	@OneToMany(mappedBy = "resposta", cascade = CascadeType.ALL)
	private List<RespostaTraducao> traducoes;

	@Transient
	private Lingua linguaTraduzida;

	public Resposta(String descricao, Pergunta pergunta) {
		super();
		this.descricao = descricao;
		this.pergunta = pergunta;
	}

	public Resposta() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idResposta) {
		this.id = idResposta;
	}

	public Pergunta getPergunta() {
		return this.pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public String getDescricao() {
		if (linguaTraduzida == null
				|| linguaTraduzida == pergunta.getEmpresa().getLinguaPadrao())
			return this.descricao;
		else {
			for (RespostaTraducao traducao : traducoes)
				if (traducao.getLingua() == linguaTraduzida
						&& traducao.getDescricao() != null)
					return traducao.getDescricao();
			for (RespostaTraducao traducao : traducoes)
				if (traducao.getLingua() == Lingua.EN
						&& traducao.getDescricao() != null)
					return traducao.getDescricao();
		}
		return this.descricao;
	}

	public String getDescricaoOriginal() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<RespostaTraducao> getTraducoes() {
		return traducoes;
	}

	public void setTraducoes(List<RespostaTraducao> traducoes) {
		this.traducoes = traducoes;
	}

	public Lingua getLinguaTraduzida() {
		return linguaTraduzida;
	}

	public void setLinguaTraduzida(Lingua linguaTraduzida) {
		this.linguaTraduzida = linguaTraduzida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Resposta other = (Resposta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}