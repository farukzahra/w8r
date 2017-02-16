package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PedidoBO;
import br.com.cardapio.bo.SolicitacaoBO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.Solicitacao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class PrincipalEmpresaMB extends BaseManagedBean<Empresa> {
    private static final long serialVersionUID = 1L;

    private List<Mesa> mesasAtivas;

    private Mesa mesaSelecionada;

    private List<Pedido> pedidosMesaSelecionada;

    private List<Pedido> pedidosNaoEntregueMesaSelecionada;

    private List<Pedido> pedidosTodos;

    private Double gastoMesaSelecionada;

    private List<Solicitacao> solicitacoes;

    private List<Mesa> mesasAbertas;

    public PrincipalEmpresaMB() {
        this.setClazz(Empresa.class);
        try {
            mesasAtivas = new MesaBO().listarPorEmpresa(getEmpresaLogada().getId());
            carregarSolicitacoes();
            carregarMesasAbertas();
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    public void carregarPoll() {
        carregarSolicitacoes();
        carregarMesasAbertas();
    }

    public void carregarSolicitacoes() {
        try {
            solicitacoes = new SolicitacaoBO().listarPorEmpresaNaoAtendidas(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    public void carregarMesasAbertas() {
        try {
            mesasAbertas = new MesaBO().listarMesasAbertas(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    public void fecharMesa(ActionEvent event) {
        try {
            new MesaBO().fecharMesa(mesaSelecionada);
            carregarMesasAbertas();
            addInfo(Mensagens.getMensagem(Mensagens.MESA_FECHADA_SUCESSO));
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
    }

    public void fecharMesas(ActionEvent event) {
        try {
            new MesaBO().fecharMesas(mesasAtivas);
            carregarMesasAbertas();
            addInfo(Mensagens.getMensagem(Mensagens.MESA_FECHADA_SUCESSO));
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
    }

    public void reAbrirMesa(ActionEvent event) {
        try {
            new MesaBO().reAbrirMesa(mesaSelecionada);
            addInfo(Mensagens.getMensagem(Mensagens.MESA_REABERTA_SUCESSO));
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
    }

    public void atenderMesa(Solicitacao solicitacao) {
        solicitacao.setStatus(Solicitacao.ATENDIDA);
        try {
            new SolicitacaoBO().merge(solicitacao);
            carregarSolicitacoes();
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError("Erro ao atender Mesa");
        } catch (IntegridadeReferencialException e) {
            e.printStackTrace();
            addError("Erro ao atender Mesa");
        }
    }

    public List<Mesa> getMesasAtivas() {
        return mesasAtivas;
    }

    public void setMesasAtivas(List<Mesa> mesasAtivas) {
        this.mesasAtivas = mesasAtivas;
    }

    public Mesa getMesaSelecionada() {
        return mesaSelecionada;
    }

    public void setMesaSelecionada(Mesa mesaSelecionada) {
        this.mesaSelecionada = mesaSelecionada;
        carregarPedidosMesaSelecionada();
        carregarGastoMesaSelecionada();
    }

    public List<Pedido> getPedidosMesaSelecionada() {
        if (pedidosMesaSelecionada == null) {
            carregarPedidosMesaSelecionada();
        }
        return this.pedidosMesaSelecionada;
    }

    public void carregarPedidosMesaSelecionada() {
        if (mesaSelecionada != null) {
            try {
                PedidoBO pedidoBO = new PedidoBO();
                pedidosMesaSelecionada = pedidoBO.buscarPedidosEntreguesPorMesa(mesaSelecionada.getId());
                pedidosNaoEntregueMesaSelecionada = pedidoBO.buscarPedidosNaoEntreguesPorMesa(mesaSelecionada.getId());
                pedidosTodos = new ArrayList<Pedido>();
                pedidosTodos.addAll(pedidosMesaSelecionada);
                pedidosTodos.addAll(pedidosNaoEntregueMesaSelecionada);
            } catch (BancoDadosException e) {
                e.printStackTrace();
                addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
            }
        }
    }

    public void carregarGastoMesaSelecionada() {
        gastoMesaSelecionada = 0D;
        for (Pedido pedido : pedidosMesaSelecionada) {
            gastoMesaSelecionada += pedido.getPreco() * pedido.getQuantidade();
        }
    }

    public Double getGastoMesaSelecionada() {
        return gastoMesaSelecionada;
    }

    public List<Pedido> getPedidosNaoEntregueMesaSelecionada() {
        if (pedidosNaoEntregueMesaSelecionada == null) {
            carregarPedidosMesaSelecionada();
        }
        return pedidosNaoEntregueMesaSelecionada;
    }

    public void setPedidosNaoEntregueMesaSelecionada(List<Pedido> pedidosNaoEntregueMesaSelecionada) {
        this.pedidosNaoEntregueMesaSelecionada = pedidosNaoEntregueMesaSelecionada;
    }

    public List<Solicitacao> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(List<Solicitacao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public List<Mesa> getMesasAbertas() {
        return mesasAbertas;
    }

    public void setMesasAbertas(List<Mesa> mesasAbertas) {
        this.mesasAbertas = mesasAbertas;
    }

    public List<Pedido> getPedidosTodos() {
        return pedidosTodos;
    }

    public void setPedidosTodos(List<Pedido> pedidosTodos) {
        this.pedidosTodos = pedidosTodos;
    }
}
