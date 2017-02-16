package br.com.cardapio.seguranca;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.managed.SessionManaged;
import br.com.cardapio.util.JSFHelper;

public class AuthorizationListener implements PhaseListener {

	private static final long serialVersionUID = -8237087853801435858L;
	private static final String[] PAGINAS_PERMITIDAS_MESA = new String[]{"/cardapio.","/pedidosmesa.","/mensagem","/pedido.","/produtosgrupo.","/configuracaomesa.","/solicitacao."};
	private static final String[] PAGINAS_PERMITIDAS_SOMENTE_ADMIN = new String[]{"/empresa."};
	private static final String[] PAGINAS_LIVRE_ACESSO = new String[]{"/loginempresa.","/logincliente.","/solicitarpermissao.","/index.","/loginmesa"};

	
	public void beforePhase(PhaseEvent event) {	
		HttpServletResponse response = JSFHelper.getResponse();
		SessionManaged sessionManaged = (SessionManaged)JSFHelper.getSession().getAttribute("sessionManaged");
		Empresa empresaLogada = sessionManaged != null ? sessionManaged.getEmpresa() : null;
		Mesa mesaLogada = sessionManaged != null ? sessionManaged.getMesa() : null;
		Cliente clienteLogado = sessionManaged != null ? sessionManaged.getCliente() : null;
		String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		try{
			// Tentou entrar em uma pagina deslogado || entrou em uma pagina logado mas que não esta autorizado	
			if (!isPaginaLivreAcesso(currentPage)) {
				if(currentPage.contains("loginmesa") && empresaLogada == null){
					JSFHelper.redirect("index.jsf");
				}
				if(isPaginaPermitidaMesa(currentPage) && mesaLogada == null && clienteLogado == null && empresaLogada != null){
					JSFHelper.redirect("loginmesa.jsf");
				}
				
				if(isPaginaPermitidaSomenteAdmin(currentPage) && (empresaLogada == null || !empresaLogada.isAdministrador()))
					JSFHelper.redirect("index.jsf");
				
				if((empresaLogada == null && mesaLogada == null && clienteLogado == null)
						|| ((mesaLogada != null || clienteLogado != null) && !isPaginaPermitidaMesa(currentPage))){
					JSFHelper.redirect("index.jsf");
				}else if((empresaLogada != null && mesaLogada == null && clienteLogado == null
						&& isPaginaPermitidaMesa(currentPage))){
					JSFHelper.redirect("logincliente.jsf");
				}else if((empresaLogada == null && mesaLogada != null 
						 && !isPaginaPermitidaMesa(currentPage))){
					JSFHelper.redirect("cardapio.jsf");
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		response.setHeader("Expires", "-1");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidade, proxy-revalidade, private, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
	}
	
	private boolean isPaginaPermitidaMesa(String paginaCorrente){
		for(String pagPermitida : PAGINAS_PERMITIDAS_MESA){
			if(paginaCorrente.contains(pagPermitida))
				return true;
		}
		return false;
	}
	
	private boolean isPaginaLivreAcesso(String paginaCorrente){
		for(String pagPermitida : PAGINAS_LIVRE_ACESSO){
			if(paginaCorrente.contains(pagPermitida))
				return true;
		}
		return false;
	}
	
	private boolean isPaginaPermitidaSomenteAdmin(String paginaCorrente){
		for(String pagPermitida : PAGINAS_PERMITIDAS_SOMENTE_ADMIN){
			if(paginaCorrente.contains(pagPermitida))
				return true;
		}
		return false;
	}

	@Override
	public void afterPhase(PhaseEvent arg0) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}