package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.Resposta;
import br.com.cardapio.exception.BancoDadosException;

public class RespostaBO extends BOBase<Resposta> {
	
	private static final long serialVersionUID = 1L;

	public RespostaBO() {
		super();
		setClazz(Resposta.class);
	}
	
	 public List<Resposta> buscarPorPergunta(int idPergunta) throws BancoDadosException{       
	        Map<String, Object> parametros = new HashMap<String, Object>();
	        parametros.put("pergunta.id", idPergunta);
	        return listByFields(parametros);     
	 }
	 
	 public List<Resposta> buscarPorEmpresa(Integer idEmpresa) throws BancoDadosException{       
	        Map<String, Object> parametros = new HashMap<String, Object>();
	        parametros.put("pergunta.empresa.id", idEmpresa);
	        String[] ordenacao = new String[]{"pergunta.descricao"};
	        return listByFields(parametros, ordenacao);     
	 }
}
