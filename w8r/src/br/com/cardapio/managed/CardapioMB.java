package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.SelectEvent;

import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.bo.ProdutoBO;
import br.com.cardapio.bo.PropagandaBO;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.Propaganda;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class CardapioMB extends BaseManagedBean<Produto> {
    private static final long serialVersionUID = 1L;

    private List<GrupoProduto> gruposProdutos;

    private GrupoProduto grupoProdutoSelecionado;

    private List<Produto> produtosDestaque;

    private List<Propaganda> propagandas;

    private List<Produto> produtosPorGrupo;

    private boolean produtoDestaque, propaganda;

    public CardapioMB() {
        this.setClazz(Produto.class);
        try {
            gruposProdutos = new GrupoProdutoBO().listarPorEmpresaEAtivo(getEmpresaReferencia().getId());
            produtosDestaque = new ArrayList<Produto>();
            propagandas = new PropagandaBO().listByEmpresa(getEmpresaReferencia().getId());
            if (propagandas != null && !propagandas.isEmpty()) {
                propaganda = true;
            }
            // setEntity(getMesaLogada(false));
            String idGrupoStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idGrupo");
            if (idGrupoStr != null) {
                Integer idGrupo = new Integer(idGrupoStr);
                GrupoProduto grupo = new GrupoProdutoBO().find(idGrupo);
                if (!gruposProdutos.contains(grupo)) {
                    JSFHelper.redirect("cardapio.jsf");
                }
                setGrupoProdutoSelecionado(new GrupoProdutoBO().find(idGrupo));
            }
            traduzirGrupoProdutos(gruposProdutos);
            traduzirProdutos(produtosPorGrupo);
            traduzirProdutos(produtosDestaque);
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA), e.getMessage());
        }
    }

    public void selecionaLinguaMesa(ValueChangeEvent event) {
        Lingua lingua = (Lingua) event.getNewValue();
        setLinguaLogada(lingua);
        traduzirGrupoProdutos(gruposProdutos);
        traduzirProdutos(produtosPorGrupo);
        traduzirProdutos(produtosDestaque);
    }

    public void selecionaTemaMesa(ValueChangeEvent event) {
        setTemaLogado((String) event.getNewValue());
    }

    public List<Produto> completaProduto(String query) {
        List<Produto> produtos = new ArrayList<Produto>();
        try {
            produtos = new ProdutoBO().buscarProdutoPorEmpresaETexto(getEmpresaReferencia().getId(), query);
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), e.getMessage());
        }
        return produtos;
    }

    public void selecionaProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        JSFHelper.redirect("pedido.jsf?idProduto=" + produto.getId());
    }

    public void fazerPedido(ActionEvent event) {
        // new PedidoBO().fazerPedido(produtoSelecionado, getMesaLogada(false), respostasCliente);
    }

    public List<GrupoProduto> getGruposProdutos() {
        return gruposProdutos;
    }

    public void setGruposProdutos(List<GrupoProduto> gruposProdutos) {
        this.gruposProdutos = gruposProdutos;
    }

    public GrupoProduto getGrupoProdutoSelecionado() {
        return grupoProdutoSelecionado;
    }

    public void setGrupoProdutoSelecionado(GrupoProduto grupoProdutoSelecionado) {
        this.grupoProdutoSelecionado = grupoProdutoSelecionado;
        try {
            produtosDestaque = new ProdutoBO().buscarProdutosEmDestaqueNoGrupo(grupoProdutoSelecionado.getId());
            produtosPorGrupo = new ProdutoBO().buscarProdutosAtivosPorGrupo(grupoProdutoSelecionado.getId());
            if (produtosDestaque != null && !produtosDestaque.isEmpty())
                produtoDestaque = true;
            traduzirProdutos(produtosPorGrupo);
            traduzirProdutos(produtosDestaque);
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    public List<Produto> getProdutos() {
        return produtosDestaque;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtosDestaque = produtos;
    }

    public List<Produto> getProdutosDestaque() {
        return produtosDestaque;
    }

    public void setProdutosDestaque(List<Produto> produtosDestaque) {
        this.produtosDestaque = produtosDestaque;
    }

    public List<Produto> getProdutosPorGrupo() {
        return produtosPorGrupo;
    }

    public void setProdutosPorGrupo(List<Produto> produtosPorGrupo) {
        this.produtosPorGrupo = produtosPorGrupo;
    }

    public boolean isProdutoDestaque() {
        return produtoDestaque;
    }

    public void setProdutoDestaque(boolean produtoDestaque) {
        this.produtoDestaque = produtoDestaque;
    }

    public List<Propaganda> getPropagandas() {
        return propagandas;
    }

    public void setPropagandas(List<Propaganda> propagandas) {
        this.propagandas = propagandas;
    }

    public boolean isPropaganda() {
        return propaganda;
    }

    public void setPropaganda(boolean propaganda) {
        this.propaganda = propaganda;
    }
}
