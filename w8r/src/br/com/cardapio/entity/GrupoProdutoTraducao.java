package br.com.cardapio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the GRUPO_PRODUTO database table.
 * 
 */
@Entity
@Table(name="GRUPO_PRODUTO_TRADUCAO")
public class GrupoProdutoTraducao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final Character STATUS_ATIVO = 'A';
	public static final Character STATUS_INATIVO = 'I';

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_GRUPO_PRODUTO_TRADUCAO", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="NM_GRUPO_PRODUTO")
	private String nome;

	@Column(name="DS_GRUPO_PRODUTO")
	private String descricao;
	
	@Column(name = "TX_LINGUA")
    private Lingua lingua;
	
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ID_GRUPO_PRODUTO")
    private GrupoProduto grupoProduto;


    public GrupoProdutoTraducao() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idGrupoProduto) {
		this.id = idGrupoProduto;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descGrupoProduto) {
		this.descricao = descGrupoProduto;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nomeGrupoProduto) {
		this.nome = nomeGrupoProduto;
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
        GrupoProdutoTraducao other = (GrupoProdutoTraducao) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public GrupoProduto getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
    }	
}