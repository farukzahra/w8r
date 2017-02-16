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


/**
 * The persistent class for the RESPOSTA database table.
 * 
 */
@Entity
@Table(name="RESPOSTA_TRADUCAO")
public class RespostaTraducao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_RESPOSTA_TRADUCAO", unique=true, nullable=false)
	private Integer id;

	@Column(name="DS_RESPOSTA")
	private String descricao;
	
	@Column(name = "TX_LINGUA")
    private Lingua lingua;

    @ManyToOne
	@JoinColumn(name="ID_RESPOSTA")
	private Resposta resposta;
    

    public RespostaTraducao() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idResposta) {
		this.id = idResposta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }
	
}