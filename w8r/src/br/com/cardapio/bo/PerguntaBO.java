package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.exception.BancoDadosException;

public class PerguntaBO extends BOBase<Pergunta> {
	
	public PerguntaBO() {
        super();
        setClazz(Pergunta.class);
    }
	
	public List<Pergunta> listarPorEmpresa(Integer idEmpresa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa);
    	return listByFields(parametros);
	}
	
	public List<Pergunta> listarComTipoRespComplexPorEmpresa(Integer idEmpresa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa);
    	parametros.put("o.tipoResposta in ('"+ Pergunta.TIPO_RESPOSTA_UMA_EM_VARIAS +"')", GenericListDAO.FILTRO_GENERICO_QUERY);
    	return listByFields(parametros);
	}

}
