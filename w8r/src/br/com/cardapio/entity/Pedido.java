package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "PEDIDO")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final char IMPRESSO = 'S';

    public static final char NAO_IMPRESSO = 'N';

    /*
     * public static final String STATUS_AGUARDANDO_ATENDIMENTO = "Aguard. Atend."; public static final String STATUS_PROCESSANDO_COZINHA =
     * "Process. Cozinha"; public static final String STATUS_AGUARDANDO_ENTREGA = "Aguard. Entrega"; public static final String STATUS_ENTREGUE
     * = "Entregue"; public static final String STATUS_CANCELADO = "Cancelado"; public static final String STATUS_PAGO_CLIENTE =
     * "Pago Cliente";
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PEDIDO", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_PEDIDO")
    private Date dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "TX_STATUS_PEDIDO")
    private StatusPedido statusPedido;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_ULTIMO_STATUS")
    private Date dataUltimoStatus;

    @Column(name = "DS_PEDIDO")
    private String descricao;

    @Column(name = "QT_MENSAGENS_GANHAS")
    private Integer qtdeMensagensGanhas;

    @Column(name = "VL_PRECO_PEDIDO", precision = 9, scale = 2)
    private Double preco;

    // bi-directional many-to-one association to Mesa
    @ManyToOne
    @JoinColumn(name = "ID_MESA")
    private Mesa mesa;

    // bi-directional many-to-one association to Produto
    @ManyToOne
    @JoinColumn(name = "ID_PRODUTO")
    private Produto produto;

    // bi-directional many-to-one association to Produto
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @Column(name = "QT_PEDIDO")
    private int quantidade = 1;

    @Transient
    private String tempoAteUltStatus;

    @Transient
    private Double precoComQtd;

    @Column(name = "DS_LOCAL")
    private String localizacao;

    @Column(name = "ST_IMPRESSAO")
    private Character impresso = NAO_IMPRESSO;

    public Pedido() {
    }

    public Long getId() {
        return this.id;
    }

    public String getTempoAteUltStatus() {
        if (statusPedido == StatusPedido.ENTREGUE) {
            long diferenca = dataUltimoStatus.getTime() - dataPedido.getTime();
            long horas = diferenca / 1000 / 60 / 60;
            long minutos = ((diferenca / 1000 / 60) % 60);
            long segundos = (diferenca / 1000) % 60;
            this.tempoAteUltStatus = (horas <= 9 ? "0" + horas : horas) + ":" + (minutos <= 9 ? "0" + minutos : minutos) + ":" + (segundos <= 9 ? "0" + segundos : segundos);
        }
        return tempoAteUltStatus;
    }

    public void setTempoAteUltStatus(String tempoAteUltStatus) {
        this.tempoAteUltStatus = tempoAteUltStatus;
    }

    public void setId(Long idPedido) {
        this.id = idPedido;
    }

    public Date getDataPedido() {
        return this.dataPedido;
    }

    public void setDataPedido(Date dtPedido) {
        this.dataPedido = dtPedido;
    }

    public StatusPedido getStatusPedido() {
        return this.statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Mesa getMesa() {
        return this.mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Date getDataUltimoStatus() {
        return dataUltimoStatus;
    }

    public void setDataUltimoStatus(Date dataUltimoStatus) {
        this.dataUltimoStatus = dataUltimoStatus;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescricaoFormatado() {
        return descricao.replaceAll("\\n", "<br/>").replaceAll("\\.", "");
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQtdeMensagensGanhas() {
        return qtdeMensagensGanhas;
    }

    public void setQtdeMensagensGanhas(Integer qtdeMensagensGanhas) {
        this.qtdeMensagensGanhas = qtdeMensagensGanhas;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public boolean isEncerrado() {
        if (statusPedido == StatusPedido.ENTREGUE || mesa.getDataUltimoFechamento().after(dataPedido))
            return true;
        else
            return false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoComQtd() {
        return preco * quantidade;
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
        Pedido other = (Pedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Character getImpresso() {
        return impresso;
    }

    public void setImpresso(Character impresso) {
        this.impresso = impresso;
    }
    
    @Transient
    public String getCorLinha(){
        if(impresso == IMPRESSO)
            return "verde";
        else
            return "branco";
    }
}
