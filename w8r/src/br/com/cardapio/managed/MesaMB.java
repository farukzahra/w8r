package br.com.cardapio.managed;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class MesaMB extends BaseManagedBean<Mesa> {
    private static final long serialVersionUID = 1L;

    public MesaMB() {
        this.setClazz(Mesa.class);
        try {
            setEntityList(new MesaBO().listarPorEmpresa(getEmpresaLogada().getId()));
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    @Override
    public void actionPersist(ActionEvent event) {
        Mesa mesa = getEntity();
        mesa.setEmpresa(getEmpresaLogada());
        if (mesa.getId() == null || mesa.getId() <= 0) {
            mesa.setDataUltimoFechamento(new Date());
            mesa.setDataUltimoFechamentoBkp(new Date());
            mesa.setQtdeMensagensGanhas(0);
            mesa.setQtdeMensagensGanhasBkp(0);
        }
        super.actionPersist(event);
    }

    public void actionGerarTodasSenhas(ActionEvent event) {
        try {
            List<Mesa> mesas = getEntityList();
            MesaBO mesaBO = new MesaBO();
            for (Mesa mesa : mesas) {
                mesa.setSenha(Mesa.getSenhaRandom());
                mesaBO.persist(mesa);
            }
            addInfo(Mensagens.getMensagem(Mensagens.REGISTRO_SALVO_COM_SUCESSO), "");
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO), "");
        }
    }

    public void actionGerarSenha(ActionEvent event) {
        try {
            MesaBO mesaBO = new MesaBO();
            getEntity().setSenha(Mesa.getSenhaRandom());
            mesaBO.persist(getEntity());
            addInfo(Mensagens.getMensagem(Mensagens.REGISTRO_SALVO_COM_SUCESSO), "");
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO), "");
        }
    }

    public void setEntityDetalhes(Mesa entity) {
        setEntity(entity);
        actionPersist(null);
    }

    public void setEntitySenha(Mesa entity) {
        setEntity(entity);
        getEntity().setSenha(Mesa.getSenhaRandom());
        actionPersist(null);
    }

    public static void main(String[] args) {
        Random gerador = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(gerador.nextInt(9999));
        }
    }
}
