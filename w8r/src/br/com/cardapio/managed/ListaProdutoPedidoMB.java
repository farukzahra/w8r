package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cardapio.entity.Pedido;

@ManagedBean
@SessionScoped
public class ListaProdutoPedidoMB extends BaseManagedBean<Pedido> {
    private static final long serialVersionUID = 1L;

    private List<Pedido> pedidos;

    private Pedido pedido;

    public ListaProdutoPedidoMB() {
        this.setClazz(Pedido.class);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        pedidos = new ArrayList<Pedido>();
        pedidos.add(pedido);
    }
}
