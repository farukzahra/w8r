package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the GRUPO_PRODUTO database table.
 */
@Entity
@Table(name = "GRUPO_PRODUTO")
public class GrupoProduto implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Character STATUS_ATIVO = 'A';

    public static final Character STATUS_INATIVO = 'I';

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO_PRODUTO", unique = true, nullable = false)
    private Integer id;

    @Column(name = "NM_GRUPO_PRODUTO")
    private String nome;

    @Column(name = "DS_GRUPO_PRODUTO")
    private String descricao;

    @Column(name = "TX_PATH_IMAGEM_GRUPO")
    private String pathImagem;

    @Column(name = "IN_STATUS", nullable = true)
    private Character status = STATUS_ATIVO;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_PROD_PAI")
    private GrupoProduto grupoProdutoPai;

    @OneToMany(mappedBy = "grupoProdutoPai")
    private List<GrupoProduto> grupoProdutosFilhos;

    @OneToMany(mappedBy = "grupoProduto")
    private List<Produto> produtos;

    @OneToMany(mappedBy = "grupoProduto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GrupoProdutoTraducao> traducoes = new ArrayList<GrupoProdutoTraducao>();

    @Transient
    private Lingua linguaTraduzida;

    private Integer ordenacao;

    public GrupoProduto(
            String nome, Empresa empresa, String pathImagem) {
        this.nome = nome;
        this.empresa = empresa;
        this.pathImagem = pathImagem;
    }

    public GrupoProduto(
            String nome, Integer ordenacao, Empresa empresa) {
        super();
        this.nome = nome;
        this.ordenacao = ordenacao;
        this.empresa = empresa;
    }

    public GrupoProduto() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idGrupoProduto) {
        this.id = idGrupoProduto;
    }

    public String getDescricao() {
        if (linguaTraduzida == null || linguaTraduzida == empresa.getLinguaPadrao())
            return this.descricao;
        else {
            for (GrupoProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == linguaTraduzida && traducao.getDescricao() != null)
                    return traducao.getDescricao();
            for (GrupoProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == Lingua.EN && traducao.getDescricao() != null)
                    return traducao.getDescricao();
        }
        return this.descricao;
    }

    public void setDescricao(String descGrupoProduto) {
        this.descricao = descGrupoProduto;
    }

    public String getNome() {
        if (linguaTraduzida == null || linguaTraduzida == empresa.getLinguaPadrao())
            return this.nome;
        else {
            for (GrupoProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == linguaTraduzida && traducao.getNome() != null)
                    return traducao.getNome();
            for (GrupoProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == Lingua.EN && traducao.getNome() != null)
                    return traducao.getNome();
        }
        return this.nome;
    }

    public void setNome(String nomeGrupoProduto) {
        this.nome = nomeGrupoProduto;
    }

    public String getPathImagem() {
        return this.pathImagem;
    }

    public void setPathImagem(String pathImagemGrupo) {
        this.pathImagem = pathImagemGrupo;
    }

    public GrupoProduto getGrupoProdutoPai() {
        return grupoProdutoPai;
    }

    public void setGrupoProdutoPai(GrupoProduto grupoProdutoPai) {
        this.grupoProdutoPai = grupoProdutoPai;
    }

    public List<GrupoProduto> getGrupoProdutosFilhos() {
        return grupoProdutosFilhos;
    }

    public void setGrupoProdutosFilhos(List<GrupoProduto> grupoProdutosFilhos) {
        this.grupoProdutosFilhos = grupoProdutosFilhos;
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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Character getStatusAtivo() {
        return STATUS_ATIVO;
    }

    public Character getStatusInativo() {
        return STATUS_INATIVO;
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
        GrupoProduto other = (GrupoProduto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public List<GrupoProdutoTraducao> getTraducoes() {
        return traducoes;
    }

    public void setTraducoes(List<GrupoProdutoTraducao> traducoes) {
        this.traducoes = traducoes;
    }

    public Lingua getLinguaTraduzida() {
        return linguaTraduzida;
    }

    public void setLinguaTraduzida(Lingua linguaTraduzida) {
        this.linguaTraduzida = linguaTraduzida;
    }

    public Integer getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(Integer ordenacao) {
        this.ordenacao = ordenacao;
    }
}
