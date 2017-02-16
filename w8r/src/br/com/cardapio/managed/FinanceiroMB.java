package br.com.cardapio.managed;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.bo.PedidoBO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@SessionScoped
public class FinanceiroMB extends BaseManagedBean<Empresa> {
	private static final long serialVersionUID = 1L;
	
	private Date dtInicial;
	private Date dtFinal;
	private List<Pedido> pedidos;
	private Double valorPagar;
	private Double valorPedidos;
	
    public FinanceiroMB() {
        this.setClazz(Empresa.class);          
        if(getEmpresaLogada().getPerfil() != Empresa.PERFIL_ADMINISTRADOR){
        	setEntity(getEmpresaLogada());
        }else{
        	try {
				setEntityList(new EmpresaBO().listAll());
			} catch (BancoDadosException e) {
				e.printStackTrace();
				addError(e.getMessage());
			}
        }
    }
    
    public void pesquisar(ActionEvent event){
    	try {
    		Empresa empresaSel = getEntity();
    		pedidos = new PedidoBO().buscarPedidosEntreguesPorEmpresaEPeriodo(empresaSel.getId(), dtInicial, dtFinal);
    		valorPedidos = 0D;
    		valorPagar = 0D;
    		for(Pedido pedido : pedidos){
    			valorPedidos += pedido.getPreco() * pedido.getQuantidade();
    			if(pedido.getQtdeMensagensGanhas() >= 1 )
    				valorPagar += empresaSel.getValorOnDemand() * pedido.getQtdeMensagensGanhas();
    			else
    				valorPagar += empresaSel.getValorOnDemand();
    		}
			addInfo(Mensagens.getMensagem(Mensagens.PESQUISA_REALIZADA));
		} catch (BancoDadosException e) {
			e.printStackTrace();
			addError(e.getMessage());
		}
    }

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public Double getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(Double valorPagar) {
		this.valorPagar = valorPagar;
	}

	public Double getValorPedidos() {
		return valorPedidos;
	}

	public void setValorPedidos(Double valorPedidos) {
		this.valorPedidos = valorPedidos;
	}
    
}
