package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import br.com.cardapio.bo.MetaLocalBO;
import br.com.cardapio.entity.MetaLocal;
import br.com.cardapio.exception.BancoDadosException;

@ManagedBean
@ViewScoped
public class MetaLocalMB extends BaseManagedBean<MetaLocal> {
    private static final long serialVersionUID = 1L;

    public MetaLocalMB() {
        this.setClazz(MetaLocal.class);
        try {
            setEntity(new MetaLocalBO().findByLingua(getLinguaLogada(), getEmpresaReferencia().getMetaLocal()));
        } catch (BancoDadosException e) {
            e.printStackTrace();
        }
    }
}
