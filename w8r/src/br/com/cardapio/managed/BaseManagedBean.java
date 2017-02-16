package br.com.cardapio.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import br.com.cardapio.bo.BOBase;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.MetaLocalBO;
import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.MetaLocal;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.Resposta;
import br.com.cardapio.entity.StatusPedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.BusinessException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;
import br.com.cardapio.util.Tradutor;

public abstract class BaseManagedBean<E extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    private BOBase<E> bo;

    private Class<E> clazz;

    private E entity;

    private List<E> entityList;

    private SessionManaged sessionManaged;
    
    private List<Lingua> linguasDisponiveis;
    
    private List<StatusPedido> statusPedidosDisponiveis = new ArrayList<StatusPedido>(Arrays.asList(StatusPedido.values()));
    
    private List<String> temasDisponiveis = Arrays.asList("a","b","c","d","e");

    @PostConstruct
    public void init() {
        getEntity();
        if(getLinguaLogada() != null) {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(getLinguaLogada().getLocale());
        }
    }

    public BaseManagedBean() {
        this.bo = new BOBase<E>();
        carregarLinguasDisponiveis();        
    }
    
    private void carregarLinguasDisponiveis(){
    	linguasDisponiveis = new ArrayList(Arrays.asList(Lingua.values()));
    }
    
    public String getResourcesBundle(String key){
    	String mensagem = Mensagens.getMensagem(key);
    	if(getEmpresaReferencia() != null) {
            try {
                MetaLocal metaLocal = new MetaLocalBO().findByLingua(getLinguaLogada(), getEmpresaReferencia().getMetaLocal());
                if (key.equals("cm.mesa"))
                    mensagem = mensagem.replace("{0}", metaLocal.getMetaLocalSingular());
                else if (key.equals("cm.mesas"))
                    mensagem = mensagem.replace("{0}", metaLocal.getMetaLocalPlural());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	return mensagem;
    }

    public Class<E> getClazz() {
        return clazz;
    }

    public void setClazz(Class<E> clazz) {
        this.clazz = clazz;
        this.bo.setClazz(clazz);
    }

    @SuppressWarnings("unchecked")
    public E getEntity() {
        if (entity == null) {
            try {
                entity = (E) Class.forName(getClazz().getName()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public void actionNew(ActionEvent event) {
        setEntity(null);
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    public void actionPersist(ActionEvent event) {
        try {
            if (bo.persist(getEntity())) {
                actionNew(event);
                addInfo(Mensagens.getMensagem(Mensagens.REGISTRO_SALVO_COM_SUCESSO), "");
            } else
                addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO), "");
        } catch (BusinessException e) {
            addWarn(e.getMensagem(), "");
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO), "");
        }
    }

    public void actionRemove(ActionEvent event) {
        try {
            if (bo.remove(getEntity())) {
                actionNew(event);
                addInfo(Mensagens.getMensagem(Mensagens.REGISTRO_REMOVIDO_COM_SUCESSO), "");
            } else
                addError(Mensagens.getMensagem(Mensagens.ERRO_AO_REMOVER_REGISTRO), "");
        } catch (IntegridadeReferencialException e) {
            //log.error("Erro no actionRemove", e);
            addError(e.getMensagem(), "");
        } catch (BancoDadosException e) {
            //log.error("Erro no actionRemove", e);
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_REMOVER_REGISTRO), "");
        }
    }

    public List<E> getEntityList() {
        return entityList;
    }

    public void validationFailed(FacesContext context, UIComponent validate) {
        ((UIInput) validate).setValid(false);
        context.validationFailed();
        context.addMessage(validate.getClientId(context), new FacesMessage(((UIInput) validate).getValidatorMessage()));
    }
    
    public void checkarNovasMensagens(){
    	try {
    	    Mesa mesaLogada = getMesaLogada(true);
			if(mesaLogada != null && mesaLogada.isFlRecebeuNovaMsg()){
				addInfo(Mensagens.getMensagem(Mensagens.MENSAGEM_NOVA), 
				        Mensagens.getMensagem(Mensagens.MENSAGEM_ACESSO_MENU_MENSAGEM));
			}
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
    }

    public void addInfo(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    public void addInfo(String summary) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, "");
    }

    public void addWarn(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    public void addWarn(String detail) {
        addMessage(FacesMessage.SEVERITY_WARN, "Atenção", detail);
    }

    public void addError(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public void addError(String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Erro", detail);
    }

    public void addFatal(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
    }

    private void addMessage(Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public Empresa getEmpresaLogada() {
        return getSessionManaged().getEmpresa();
    }

    public void setEmpresaLogada(Empresa empresaLogada) {
        this.getSessionManaged().setEmpresa(empresaLogada);
        if(empresaLogada != null) {
        	this.setLinguaLogada(empresaLogada.getLinguaPadrao());
        	this.setTemaLogado(empresaLogada.getTema());
        }
    }

    public Mesa getMesaLogada(boolean forceRefresh) throws BancoDadosException {
        if (forceRefresh){
            Mesa mesa = new MesaBO().find(getSessionManaged().getMesa());
            getSessionManaged().setMesa(mesa);
            return mesa;
        }else
            return getSessionManaged().getMesa();
    }
    
    public Empresa getEmpresaReferencia(){
    	if(getEmpresaLogada() != null)
    		return getEmpresaLogada();
    	else if(getMesaLogada() != null)
    		return getMesaLogada().getEmpresa();
    	else if(getClienteLogado() != null)
    		return getClienteLogado().getEmpresaLogada();
    	else return null;
    }

    public Mesa getMesaLogada(){
        return getSessionManaged().getMesa();
    }

    public void setMesaLogada(Mesa mesalogada) {
        this.getSessionManaged().setMesa(mesalogada);
    }
    
    public Cliente getClienteLogado(){
        return getSessionManaged().getCliente();
    }

    public void setClienteLogado(Cliente cliente) {
        this.getSessionManaged().setCliente(cliente);
    }
    
    public Lingua getLinguaLogada(){
        Lingua lingua = getSessionManaged().getLingua();
        return lingua;
    }
    
    public void setLinguaLogada(Lingua lingua){        
        getSessionManaged().setLingua(lingua);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(lingua.getLocale());
    }
    
    public String getTemaLogado(){
        return getSessionManaged().getTema();
    }
    
    public void setTemaLogado(String temaLogado){        
        getSessionManaged().setTema(temaLogado);
    }
    
    public boolean isFlTraducaoNecessaria(){
        return true;
        //getSessionManaged().getLingua() != getMesaLogada().getEmpresa().getLinguaPadrao();
    }

    public BOBase<E> getBo() {
        return bo;
    }

    public void setBo(BOBase<E> bO) {
        this.bo = bO;
    }

    public SessionManaged getSessionManaged() {
        HttpSession httpSession = JSFHelper.getSession();
        sessionManaged = (SessionManaged) httpSession.getAttribute("sessionManaged");
        if (sessionManaged == null) {
            sessionManaged = new SessionManaged();
            httpSession.setAttribute("sessionManaged", sessionManaged);
        }
        return sessionManaged;
    }

    public void setSessionManaged(SessionManaged session) {
        this.sessionManaged = session;
    }

    public void setEntityList(List<E> entityList) {
        this.entityList = entityList;
    }

    public List<Lingua> getLinguasDisponiveis() {
        return linguasDisponiveis;
    }

    public void setLinguasDisponiveis(List<Lingua> linguasDisponiveis) {
        this.linguasDisponiveis = linguasDisponiveis;
    }
    
    public List<Lingua> getLinguasDisponiveisSemPrincipal() {
    	List<Lingua> linguas = new ArrayList(Arrays.asList(Lingua.values()));
    	if(getEmpresaLogada() != null && linguas.contains(getEmpresaLogada().getLinguaPadrao()))
    		linguas.remove(getEmpresaLogada().getLinguaPadrao());
        return linguas;
    }
    
    public void traduzirProdutos(List<Produto> produtos){
    	Tradutor.traduzirProdutos(produtos, getLinguaLogada());
    }
    
    public void traduzirGrupoProdutos(List<GrupoProduto> grupos){
    	Tradutor.traduzirGrupoProdutos(grupos, getLinguaLogada());
    }
    
    public void traduzirRespostas(List<Resposta> respostas){
    	Tradutor.traduzirRespostas(respostas, getLinguaLogada());
    }
    
    public void traduzirPerguntas(List<Pergunta> perguntas){
    	Tradutor.traduzirPerguntas(perguntas, getLinguaLogada());
    }
    
    public void traduzirPedidos(List<Pedido> pedidos){
    	Tradutor.traduzirPedidos(pedidos, getLinguaLogada());
    }
    
    public void traduzirPedido(Pedido pedido){
    	Tradutor.traduzirPedido(pedido, getLinguaLogada());
    }

	public List<StatusPedido> getStatusPedidosDisponiveis() {
		return statusPedidosDisponiveis;
	}

	public void setStatusPedidosDisponiveis(
			List<StatusPedido> statusPedidosDisponiveis) {
		this.statusPedidosDisponiveis = statusPedidosDisponiveis;
	}

	public List<String> getTemasDisponiveis() {
		return temasDisponiveis;
	}

	public void setTemasDisponiveis(List<String> temasDisponiveis) {
		this.temasDisponiveis = temasDisponiveis;
	}
	
	public boolean isFazerPedidos(){
	    return getMesaLogada() != null;
	}
}