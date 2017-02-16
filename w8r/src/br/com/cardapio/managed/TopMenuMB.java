package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.cardapio.util.JSFHelper;

@ManagedBean
@RequestScoped
public class TopMenuMB {

	private String paginaAtual;

	public String getPaginaAtual() {
		return JSFHelper.getExternalContext().getRequestServletPath().replace("/pages/","");
	}

	public void setPaginaAtual(String paginaAtual) {
		this.paginaAtual = paginaAtual;
	}
}
