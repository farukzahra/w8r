package br.com.cardapio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the PRODUTO database table.
 */
@Entity
@Table(name = "PRODUTO_TRADUCAO")
public class ProdutoTraducao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUTO_TRADUCAO", unique = true, nullable = false)
    private Integer id;

    @Column(name = "DS_PRODUTO")
    private String descricao;

    @Column(name = "NM_PRODUTO")
    private String nome;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TX_LINGUA")
    private Lingua lingua;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ID_PRODUTO")
    private Produto produto;

    public ProdutoTraducao() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idProduto) {
        this.id = idProduto;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String DescProduto) {
        this.descricao = DescProduto;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String NomeProduto) {
        this.nome = NomeProduto;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    
}
