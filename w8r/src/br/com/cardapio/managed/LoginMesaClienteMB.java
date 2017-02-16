package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.ClienteBO;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.BusinessException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@SessionScoped
public class LoginMesaClienteMB extends BaseManagedBean<Cliente> {

    private static final long serialVersionUID = 1L;
    private List<Mesa> mesasDisp = new ArrayList<Mesa>();
    private Mesa mesaSel = new Mesa();
    
    public LoginMesaClienteMB() {
        setClazz(Cliente.class); 
        setBo(new ClienteBO());
        try {
        	MesaBO bo = new MesaBO();
        	mesasDisp = bo.listarAtivasPorEmpresa(getEmpresaReferencia().getId());
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
    }
    
    @Override
    public ClienteBO getBo() {
    	return ((ClienteBO)super.getBo());
    }
    
    public String actionLogin(){
		try {
			Cliente clienteLogando = getBo().buscarPorLoginValidandoSenha(getEmpresaReferencia().getId(), getEntity().getLogin(), getEntity().getSenha());
			if(mesaSel != null && mesaSel.getId() > 0 && clienteLogando != null){          
	    		setClienteLogado(clienteLogando); 
	    		setMesaLogada(mesaSel);
	    		setEmpresaLogada(null);
	    		JSFHelper.redirect("cardapio.jsf?faces-redirect=true");
	    	}else{
	    		addError(Mensagens.getMensagem(Mensagens.USUARIO_SENHA_INVALIDO));
	    	}
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		} catch (BusinessException e) {
			addError(e.getMensagem());
		}		
		return "loginmesacliente.xhtml";
    }
    
    public void actionLogoff(){
    	setMesaLogada(null);
    	setClienteLogado(null);
    	JSFHelper.redirect("loginempresa.jsf?faces-redirect=true");
    }
    

	public List<Mesa> getMesasDisp() {
		return mesasDisp;
	}

	public void setMesasDisp(List<Mesa> mesasDisp) {
		this.mesasDisp = mesasDisp;
	}

	public Mesa getMesaSel() {
		return mesaSel;
	}

	public void setMesaSel(Mesa mesaSel) {
		this.mesaSel = mesaSel;
	}
    
}