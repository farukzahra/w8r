package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PedidoBO;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.StatusPedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class ListaPedidoMB extends BaseManagedBean<Pedido> {
    private static final long serialVersionUID = 1L;

    private List<StatusPedido> statusFiltro = new ArrayList<StatusPedido>();

    private List<Pedido> pedidos = new ArrayList<Pedido>();

    private String filtroProduto;

    public ListaPedidoMB() {
        this.setClazz(Pedido.class);
        statusFiltro.add(StatusPedido.AGUARDANDO_ATENDIMENTO);
        statusFiltro.add(StatusPedido.AGUARDANDO_ENTREGA);
        statusFiltro.add(StatusPedido.PROCESSANDO_COZINHA);
        carregarPedidos();
    }
    
    @Override
    public void setEntity(Pedido entity) {
        // TODO Auto-generated method stub
        super.setEntity(entity);
    }
    
    public void actionMarcaImpressao(){
        getEntity().setImpresso(Pedido.IMPRESSO);
        try {
            getBo().persist(getEntity());
        } catch (BancoDadosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IntegridadeReferencialException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RegistroExistenteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public Pedido getEntity() {
        // TODO Auto-generated method stub
        return super.getEntity();
    }

    public void carregarPedidos() {
        try {
            pedidos = new PedidoBO().buscarPedidosPorStatus(statusFiltro, getEmpresaLogada().getId(), filtroProduto);
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    public void carregarPedidosEvent(ValueChangeEvent event) {
        statusFiltro = (List<StatusPedido>) event.getNewValue();
        carregarPedidos();
    }
    
    public void actionCarregarPedidos(ActionEvent event) {
        carregarPedidos();
    }

    public void carregarPedidosEvent() {
        carregarPedidos();
    }
    
    public String listaPedidoProduto(){
        return "listaprodutopedido.jsf";
    }

    
    public void trocarStatus(Pedido pedidoSelecionado){
        try {
            if (pedidoSelecionado != null) {
                pedidoSelecionado.setDataUltimoStatus(new Date());
                if (StatusPedido.ENTREGUE.equals(pedidoSelecionado.getStatusPedido())) {
                    new MesaBO().addMensagensPorPedido(pedidoSelecionado);
                }
                new PedidoBO().merge(pedidoSelecionado);
                carregarPedidos();
                addInfo(Mensagens.getMensagem(Mensagens.STATUS_TROCADO_SUCESSO));
            }
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
    }

    public List<StatusPedido> getStatusFiltro() {
        return statusFiltro;
    }

    public void setStatusFiltro(List<StatusPedido> statusFiltro) {
        this.statusFiltro = statusFiltro;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getFiltroProduto() {
        return filtroProduto;
    }

    public void setFiltroProduto(String filtroProduto) {
        this.filtroProduto = filtroProduto;
    }
}
