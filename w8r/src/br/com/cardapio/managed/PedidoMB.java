package br.com.cardapio.managed;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PedidoBO;
import br.com.cardapio.bo.ProdutoBO;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.StatusPedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class PedidoMB extends BaseManagedBean<Pedido> {
    private static final long serialVersionUID = 1L;

    private Double totalPedido;

    private List<Pedido> pedidosMesa;

    private int quantidade = 1;

    public PedidoMB() {
        this.setClazz(Pedido.class);
        Pedido pedido = getEntity();
        try {
            pedido.setMesa(getMesaLogada(false));
            String idProdutoStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idProduto");
            if (idProdutoStr != null) {
                Integer idProduto = new Integer(idProdutoStr);
                Produto produto = new ProdutoBO().find(idProduto);
                pedido.setProduto(produto);
            }
            traduzirPedido(pedido);
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        }
    }
    
    public boolean validaMesa(){
        try {
            Mesa mesaLogando = new MesaBO().buscarPorNumeroMesaValidandoSenha(getEmpresaReferencia().getId(), getMesaLogada().getNumeroMesa(), getMesaLogada().getSenha());
            return mesaLogando != null;
        } catch (BancoDadosException e) {
            addError(e.getMessage());
        }       
        return false;
    }

    public String fazerPedido(ActionEvent event) {
        try {
            if(validaMesa()) {
                new PedidoBO().fazerPedido(getEntity().getProduto(), getMesaLogada(false), getClienteLogado(), quantidade);
                addInfo(Mensagens.getMensagem(Mensagens.PEDIDO_REALIZADO_SUCESSO));
                JSFHelper.redirect("cardapio.jsf?faces-redirect=true");
            }else {
                setMesaLogada(null);
                setClienteLogado(null);
                JSFHelper.redirect("logincliente.jsf?faces-redirect=true");
            }
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
        return "";
    }

    public List<Pedido> getPedidosMesa() {
        return pedidosMesa;
    }

    public Double getTotalPedido() {
        try {
            totalPedido = 0d;
            Mesa mesaLogada = getMesaLogada(false);
            if (mesaLogada != null) {
                this.pedidosMesa = new PedidoBO().buscarPedidosPorMesa(getMesaLogada(false).getId());
                if (pedidosMesa != null && !pedidosMesa.isEmpty()) {
                    for (Pedido pedido : pedidosMesa) {
                        if (!StatusPedido.CANCELADO.equals(pedido.getStatusPedido())) {
                            totalPedido += pedido.getPreco() * pedido.getQuantidade();
                        }
                    }
                    traduzirPedidos(pedidosMesa);
                }
            }
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        }
        return totalPedido;
    }

    public void setTotalPedido(Double totalPedido) {
        this.totalPedido = totalPedido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
