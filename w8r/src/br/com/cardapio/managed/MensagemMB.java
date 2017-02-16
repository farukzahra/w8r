package br.com.cardapio.managed;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.BOBase;
import br.com.cardapio.bo.MensagemBO;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Mensagem;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.CreditosInsuficException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.Mensagens;

@ViewScoped
@ManagedBean
public class MensagemMB extends BaseManagedBean<Mensagem> {
    private List<Mensagem> dialogoMesa;

    private List<Mesa> mesasDispDialogo;

    public MensagemMB() {
        this.setClazz(Mensagem.class);
        setBo(new MensagemBO());
        carregarTela();
    }

    private void carregarTela() {
        try {
            mesasDispDialogo = new MesaBO().listarPorEmpresa(getEmpresaReferencia().getId());
            mesasDispDialogo.remove(getMesaLogada(false));
            reloadConversa();
            Mesa mesaLogada = getMesaLogada(true);
            getEntity().setMesaOrigem(mesaLogada);
            if (mesaLogada != null && !mesaLogada.isTemCreditoMensagens())
                addWarn(new CreditosInsuficException().getMensagem());
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }

    public void enviarMensagem(ActionEvent event) {
        try {
            MensagemBO bo = new MensagemBO();
            bo.enviarMensagem(getMesaLogada(true), getEntity().getMesaDestino(), getEntity().getDescricao());
            bo.flegarLeituraBatch(dialogoMesa, getMesaLogada(false).getId());
            getEntity().setDescricao("");
            addInfo(Mensagens.getMensagem(Mensagens.MENSAGEM_ENVIADA));
            reloadConversa();
        } catch (CreditosInsuficException e) {
            addWarn(e.getMensagem());
        } catch (BancoDadosException e) {
            addError(e.getMessage());
            e.printStackTrace();
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
            e.printStackTrace();
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
            e.printStackTrace();
        }
    }

    public void reloadConversa() {
        try {
            Mesa mesaLogada = getMesaLogada(false);
            if (mesaLogada != null && getEntity().getMesaDestino() != null) {
                MensagemBO bo = new MensagemBO();
                dialogoMesa = bo.buscarDialogoMesas(mesaLogada.getId(), getEntity().getMesaDestino().getId());
                bo.flegarLeituraBatch(dialogoMesa, mesaLogada.getId());
            }
            verificarNovasMensagens(mesaLogada);
        } catch (BancoDadosException e) {
            addError(e.getMessage());
            e.printStackTrace();
        } catch (IntegridadeReferencialException e) {
            addError(e.getMessage());
        }
    }

    @Override
    public MensagemBO getBo() {
        return (MensagemBO) super.getBo();
    }

    private void verificarNovasMensagens(Mesa mesaLogada) {
        try {
            if (mesasDispDialogo != null && mesaLogada != null) {
                List<Mensagem> msgsRecebidas = getBo().buscarRecebidasPorMesa(mesaLogada.getId());
                for (Mensagem msgRecebida : msgsRecebidas) {
                    for (Mesa mesa : mesasDispDialogo) {
                        if (mesa.equals(msgRecebida.getMesaOrigem()) && !msgRecebida.isFlLeitura()) {
                            mesa.setFlEnviouNovaMsg(true);
                            addInfo(Mensagens.getMensagem(Mensagens.MENSAGEM_NOVA));
                            break;
                        }
                    }
                }
            }
            // reloadConversa();
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }

    public List<Mensagem> getDialogoMesa() {
        // reloadConversa();
        return dialogoMesa;
    }

    public void setDialogoMesa(List<Mensagem> dialogoMesa) {
        this.dialogoMesa = dialogoMesa;
    }

    public List<Mesa> getMesasDispDialogo() {
        return mesasDispDialogo;
    }

    public void setMesasDispDialogo(List<Mesa> mesasDispDialogo) {
        this.mesasDispDialogo = mesasDispDialogo;
    }

    public void setMesaDestino(Mesa mesa) {
        getEntity().setMesaDestino(mesa);
        reloadConversa();
    }
}
