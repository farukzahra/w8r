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
 * The persistent class for the PERGUNTA database table.
 */
@Entity
@Table(name = "PERGUNTA_TRADUCAO")
public class PerguntaTraducao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERGUNTA_TRADUCAO", unique = true, nullable = false)
    private Integer id;

    @Column(name = "DS_PERGUNTA")
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "ID_PERGUNTA")
    private Pergunta pergunta;
    
    @Column(name = "TX_LINGUA")
    private Lingua lingua;

    public PerguntaTraducao() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idPergunta) {
        this.id = idPergunta;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String DescPergunta) {
        this.descricao = DescPergunta;
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
        PerguntaTraducao other = (PerguntaTraducao) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

	public Lingua getLingua() {
		return lingua;
	}

	public void setLingua(Lingua lingua) {
		this.lingua = lingua;
	}
}
