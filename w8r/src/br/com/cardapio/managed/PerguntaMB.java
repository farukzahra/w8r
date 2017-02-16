package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.PerguntaBO;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.PerguntaTraducao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Tradutor;

@ManagedBean
@ViewScoped
public class PerguntaMB extends BaseManagedBean<Pergunta> {
	
	private List<PerguntaTraducao> traducoes;
	
    private Integer progress = 0;
    
    public PerguntaMB() {
        this.setClazz(Pergunta.class);
        this.getEntity().setEmpresa(getEmpresaLogada());
        carregarTraducoes(getEntity());
        try{
        	setEntityList(new PerguntaBO().listarPorEmpresa(getEmpresaLogada().getId()));
        }catch (BancoDadosException e) {
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
        for(PerguntaTraducao traducao : traducoes){
            progress += somatorio+1;
            try {
                traducao.setDescricao(Tradutor.traduzirAuto(descricao, linguaPadrao, traducao.getLingua()));
            } catch (Exception e) {
                e.printStackTrace();
            }               
        }
    }
    
    @Override
    public void setEntity(Pergunta entity) {
    	super.setEntity(entity);
    	carregarTraducoes(entity);
    }
    
    @Override
    public void actionPersist(ActionEvent event) {
        setTraducoes(getEntity());
        super.actionPersist(event);
    }
    
    private void setTraducoes(Pergunta pergunta) {
    	List<PerguntaTraducao> pts = new ArrayList<PerguntaTraducao>();
    	for(PerguntaTraducao pt : traducoes){
    		if(pt.getDescricao() != null && !pt.getDescricao().isEmpty()){
    			pts.add(pt);
    		}
    	}
    	pergunta.setTraducoes(pts);
    }

	private void carregarTraducoes(Pergunta pergunta) {
		traducoes = new ArrayList<PerguntaTraducao>();
		List<Lingua> linguas = getLinguasDisponiveisSemPrincipal();
		for(Lingua lingua : linguas){
			PerguntaTraducao pt = new PerguntaTraducao();
		    pt.setPergunta(pergunta);
		    pt.setLingua(lingua);
		    if(pergunta != null && pergunta.getTraducoes() != null)
			    for(PerguntaTraducao trad : pergunta.getTraducoes()){
			        if(lingua.equals(trad.getLingua())){
			            pt = trad;
			        }
			    }
		    traducoes.add(pt);
		}
	}

	public List<PerguntaTraducao> getTraducoes() {
		return traducoes;
	}

	public void setTraducoes(List<PerguntaTraducao> traducoes) {
		this.traducoes = traducoes;
	}
}
