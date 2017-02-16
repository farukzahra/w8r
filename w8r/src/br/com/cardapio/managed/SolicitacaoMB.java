package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cardapio.entity.Solicitacao;

@ManagedBean
@ViewScoped
public class SolicitacaoMB extends BaseManagedBean<Solicitacao> {
    private static final long serialVersionUID = 1L;

    public SolicitacaoMB() {
        this.setClazz(Solicitacao.class);
        getEntity().setMesa(getMesaLogada());
        getEntity().setStatus(Solicitacao.NAO_ATENDIDA);
    }
}
