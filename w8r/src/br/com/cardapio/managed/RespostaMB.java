package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.PerguntaBO;
import br.com.cardapio.bo.RespostaBO;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Resposta;
import br.com.cardapio.entity.RespostaTraducao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Tradutor;

@ManagedBean
@ViewScoped
public class RespostaMB extends BaseManagedBean<Resposta> {

    private static final long serialVersionUID = 1L;    
    private List<Pergunta> perguntas;    
    private List<RespostaTraducao> traducoes;    
    private Integer progress = 0;
    
    public RespostaMB() {
        this.setClazz(Resposta.class);
        try {
			perguntas = new PerguntaBO().listarComTipoRespComplexPorEmpresa(getEmpresaLogada().getId());
			List<Resposta> respostas = new RespostaBO().buscarPorEmpresa(getEmpresaLogada().getId());
			setEntityList(respostas);
			carregarTraducoes(getEntity());
		} catch (BancoDadosException e) {
			e.printStackTrace();
			addError(e.getMessage());
		}
    }
    
    public Integer getProgress() {
        if(progress > 100){
           progress = 0;
           return 100; 
        }else{
            return progress;
        }
    }
    
    public void traduzCampos(){
        String descricao = getEntity().getDescricao();
        Lingua linguaPadrao = getEmpresaLogada().getLinguaPadrao();
        int somatorio = 100/traducoes.size();
        progress = 0;
        for(RespostaTraducao traducao : traducoes){
            progress += somatorio+1;
            try {
                traducao.setDescricao(Tradutor.traduzirAuto(descricao, linguaPadrao, traducao.getLingua()));
            } catch (Exception e) {
                e.printStackTrace();
            }               
        }
    }
    
    @Override
    public void setEntity(Resposta entity) {
    	super.setEntity(entity);
    	carregarTraducoes(entity);
    }
    
    @Override
    public void actionPersist(ActionEvent event) {
        setTraducoes(getEntity());
        super.actionPersist(event);
    }
    
    public void setPerguntaSelecionada(Pergunta pergunta){
    	try {
			setEntityList(new RespostaBO().buscarPorPergunta(pergunta.getId()));
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
    }
    
    private void setTraducoes(Resposta resposta) {
    	List<RespostaTraducao> pts = new ArrayList<RespostaTraducao>();
    	for(RespostaTraducao pt : traducoes){
    		if(pt.getDescricao() != null && !pt.getDescricao().isEmpty()){
    			pts.add(pt);
    		}
    	}
    	resposta.setTraducoes(pts);
    }

	private void carregarTraducoes(Resposta resposta) {
		traducoes = new ArrayList<RespostaTraducao>();
		List<Lingua> linguas = getLinguasDisponiveisSemPrincipal();
		for(Lingua lingua : linguas){
			RespostaTraducao pt = new RespostaTraducao();
		    pt.setResposta(resposta);
		    pt.setLingua(lingua);
		    if(resposta != null && resposta.getTraducoes() != null)
			    for(RespostaTraducao trad : resposta.getTraducoes()){
			        if(lingua.equals(trad.getLingua())){
			            pt = trad;
			        }
			    }
		    traducoes.add(pt);
		}
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	public List<RespostaTraducao> getTraducoes() {
		return traducoes;
	}

	public void setTraducoes(List<RespostaTraducao> traducoes) {
		this.traducoes = traducoes;
	}
}
