package br.com.cardapio.entity;

import java.io.Serializable;
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
 * The persistent class for the PRODUTO database table.
 */
@Entity
@Table(name = "PRODUTO")
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Character STATUS_ATIVO = 'A';

    public static final Character STATUS_INATIVO = 'I';

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUTO", unique = true, nullable = false)
    private Integer id;

    @Column(name = "DS_PRODUTO")
    private String descricao;

    @Column(name = "NM_PRODUTO")
    private String nome;

    @Column(name = "ds_codigo_externo")
    private String codExterno;

    @Column(name = "QT_MENSAGENS_DADAS")
    private Integer qtdeMensagensDadas = 1;

    @Column(name = "TX_PATH_IMAGEM_DETALHADA")
    private String pathImagemDetalhada;

    @Column(name = "TX_PATH_IMAGEM_PRINCIPAL")
    private String pathImagemPrincipal;

    @Column(name = "TX_URL_VIDEO")
    private String urlVideo;

    @Column(name = "VL_PRECO_PRODUTO", precision = 9, scale = 2)
    private Double preco;

    @Column(name = "IN_STATUS", nullable = true)
    private Character status = STATUS_ATIVO;

    @Column(name = "FL_DESTAQUE")
    private boolean flDestaque;

    @Column(name = "FL_AVALIAVEL")
    private boolean flAvaliavel;

    @Column(name = "VL_AVALIACAO_MEDIA", precision = 2, scale = 2)
    private Double avaliacaoMedia;

    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_PRODUTO")
    private GrupoProduto grupoProduto;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProdutoTraducao> traducoes;

    // bi-directional many-to-one association to Pedido
    @OneToMany(mappedBy = "produto")
    private List<Pedido> pedidos;

    // bi-directional many-to-one association to ProdutoPergunta
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ProdutoPergunta> produtoPerguntas;

    @Transient
    private Lingua linguaTraduzida;

    private Integer ordenacao;

    public Produto(
            String nome, String descricao, Double preco, GrupoProduto grupoProduto, boolean destaque, String pathImagemDetalhada,
            String pathImagemPrincipal) {
        super();
        this.descricao = descricao;
        this.nome = nome;
        this.preco = preco;
        this.grupoProduto = grupoProduto;
        this.flDestaque = destaque;
        this.pathImagemDetalhada = pathImagemDetalhada;
        this.pathImagemPrincipal = pathImagemPrincipal;
    }

    public Produto(
            String codExterno, String nome, Integer ordenacao, String descricao, Double preco, Integer qtdeMensagensDadas, boolean flDestaque, GrupoProduto grupoProduto) {
        super();
        this.descricao = descricao;
        this.nome = nome;
        this.codExterno = codExterno;
        this.qtdeMensagensDadas = qtdeMensagensDadas;
        this.preco = preco;
        this.flDestaque = flDestaque;
        this.ordenacao = ordenacao;
        this.grupoProduto = grupoProduto;
    }

    public Produto() {
        super();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idProduto) {
        this.id = idProduto;
    }

    public String getDescricao() {
        if (linguaTraduzida == null || linguaTraduzida == grupoProduto.getEmpresa().getLinguaPadrao())
            return this.descricao;
        else {
            for (ProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == linguaTraduzida && traducao.getDescricao() != null)
                    return traducao.getDescricao();
            for (ProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == Lingua.EN && traducao.getDescricao() != null)
                    return traducao.getDescricao();
        }
        return this.descricao;
    }

    public void setDescricao(String DescProduto) {
        this.descricao = DescProduto;
    }

    public String getNome() {
        if (linguaTraduzida == null || linguaTraduzida == grupoProduto.getEmpresa().getLinguaPadrao())
            return this.nome;
        else {
            for (ProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == linguaTraduzida && traducao.getNome() != null)
                    return traducao.getNome();
            for (ProdutoTraducao traducao : traducoes)
                if (traducao.getLingua() == Lingua.EN && traducao.getNome() != null)
                    return traducao.getNome();
        }
        return this.nome;
    }

    public void setNome(String NomeProduto) {
        this.nome = NomeProduto;
    }

    public Integer getQtdeMensagensDadas() {
        return this.qtdeMensagensDadas;
    }

    public void setQtdeMensagensDadas(Integer qtdeMensagensGanhas) {
        this.qtdeMensagensDadas = qtdeMensagensGanhas;
    }

    public String getPathImagemDetalhada() {
        if (pathImagemDetalhada != null)
            return this.pathImagemDetalhada;
        else
            return "/sem_imagem.png";
    }

    public void setPathImagemDetalhada(String pathImagemDetalhada) {
        this.pathImagemDetalhada = pathImagemDetalhada;
    }

    public String getPathImagemPrincipal() {
        if (this.pathImagemPrincipal != null)
            return this.pathImagemPrincipal;
        else if (this.pathImagemDetalhada != null)
            return this.pathImagemDetalhada;
        else
            return "/sem_imagem.png";
    }

    public void setPathImagemPrincipal(String pathImagemPrincipal) {
        this.pathImagemPrincipal = pathImagemPrincipal;
    }

    public String getUrlVideo() {
        return this.urlVideo;
    }

    public void setUrlVideo(String pathVideo) {
        this.urlVideo = pathVideo;
    }

    public Double getPreco() {
        return this.preco;
    }

    public void setPreco(Double valorPrecoProduto) {
        this.preco = valorPrecoProduto;
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<ProdutoPergunta> getProdutoPerguntas() {
        return this.produtoPerguntas;
    }

    public void setProdutoPerguntas(List<ProdutoPergunta> produtoPerguntas) {
        this.produtoPerguntas = produtoPerguntas;
    }

    public GrupoProduto getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public Character getStatusAtivo() {
        return STATUS_ATIVO;
    }

    public Character getStatusInativo() {
        return STATUS_INATIVO;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
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
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public boolean getFlDestaque() {
        return flDestaque;
    }

    public void setFlDestaque(boolean flDestaque) {
        this.flDestaque = flDestaque;
    }

    public boolean getFlAvaliavel() {
        return flAvaliavel;
    }

    public void setFlAvaliavel(boolean flAvaliavel) {
        this.flAvaliavel = flAvaliavel;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public List<ProdutoTraducao> getTraducoes() {
        return traducoes;
    }

    public void setTraducoes(List<ProdutoTraducao> traducoes) {
        this.traducoes = traducoes;
    }

    public Lingua getLinguaTraduzida() {
        return linguaTraduzida;
    }

    public void setLinguaTraduzida(Lingua linguaTraduzida) {
        this.linguaTraduzida = linguaTraduzida;
    }

    public String getCodExterno() {
        return codExterno;
    }

    public void setCodExterno(String codExterno) {
        this.codExterno = codExterno;
    }

    public String getNomeComCodigo() {
        return codExterno != null && !codExterno.isEmpty() ? codExterno + " - " + nome : nome;
    }

    public Integer getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(Integer ordenacao) {
        this.ordenacao = ordenacao;
    }
}
