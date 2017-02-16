package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.MetaLocal;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@SessionScoped
public class LoginMesaMB extends BaseManagedBean<Mesa> {

    private static final long serialVersionUID = 1L;
    private List<Mesa> mesasDisp = new ArrayList<Mesa>();
    
    public LoginMesaMB() {
        setClazz(Mesa.class); 
        MesaBO bo = new MesaBO();
        setBo(bo);
        try {
        	mesasDisp = getBo().listarAtivasPorEmpresa(getEmpresaReferencia().getId());
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
    }
    
    @Override
    public MesaBO getBo() {
    	return ((MesaBO)super.getBo());
    }

    public String actionLoginMesa(){
		try {
			Mesa mesaLogando = getBo().buscarPorNumeroMesaValidandoSenha(getEmpresaReferencia().getId(), getEntity().getNumeroMesa(), getEntity().getSenha());
			if(mesaLogando != null){        			    
	    		setMesaLogada(mesaLogando); 
	    		setEmpresaLogada(null);
	    		if(getEmpresaReferencia().getMetaLocal().equals(MetaLocal.QUARTO)){
	    		    mesaLogando.setDescricao(getEntity().getDescricao());
	    		    try {
                        getBo().persist(mesaLogando);
                    } catch (IntegridadeReferencialException e) {
                        e.printStackTrace();
                    } catch (RegistroExistenteException e) {
                        e.printStackTrace();
                    }
	    		}
	    		JSFHelper.redirect("cardapio.jsf?faces-redirect=true");
	    	}else{
	    		addError(Mensagens.getMensagem(Mensagens.USUARIO_SENHA_INVALIDO));
	    	}
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}		
		return "loginmesa.xhtml";
    }
    
    public void actionLogoff(){
    	setMesaLogada(null);
    	setClienteLogado(null);
    	JSFHelper.redirect("loginempresa.jsf?faces-redirect=true");
    }
    
    @Override
    public void actionPersist(ActionEvent event) {
    	// TODO Auto-generated method stub
    	super.actionPersist(event);
    }
    
    public void redirectComNumeroMesa(ActionEvent event){
    	Integer numeroMesa = (Integer)JSFHelper.getActionAttribute(event, "numeroMesa");
    	getEntity().setNumeroMesa(numeroMesa);
    	JSFHelper.redirect("loginmesa.jsf?faces-redirect=true");
    }

	public List<Mesa> getMesasDisp() {
		return mesasDisp;
	}

	public void setMesasDisp(List<Mesa> mesasDisp) {
		this.mesasDisp = mesasDisp;
	}
    
}