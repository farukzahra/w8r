package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import br.com.cardapio.bo.PermissaoBO;
import br.com.cardapio.entity.Permissao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class PermissaoMB extends BaseManagedBean<Permissao> {
	
	public PermissaoMB() {
        this.setClazz(Permissao.class);
        try {
        	carregarPermissoesEmAberto();
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
	}
	
	public void carregarPermissoesEmAberto() throws BancoDadosException{
		setEntityList(new PermissaoBO().listByEmpresaEAbertas(getEmpresaReferencia().getId()));
	}
	
	public void trocarStatus(ValueChangeEvent event) {
		try {
			String idPermissaoStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPermissao");
			if(idPermissaoStr != null){
				boolean novoStatus = (Boolean)event.getNewValue();
				Integer idPermissao = new Integer(idPermissaoStr);
				PermissaoBO bo = new PermissaoBO();
				Permissao permissao = bo.find(idPermissao);
				permissao.setFlAprovado(novoStatus);
				bo.merge(permissao);		
				carregarPermissoesEmAberto();
				addInfo(Mensagens.getMensagem(Mensagens.STATUS_TROCADO_SUCESSO));
			}
		} catch (BancoDadosException e) {
			e.printStackTrace();
			addError(e.getMessage());
		} catch (IntegridadeReferencialException e) {
			addError(e.getMensagem());
		}
	}

}
