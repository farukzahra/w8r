package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.PlantaSalao;
import br.com.cardapio.exception.BancoDadosException;

public class PlantaSalaoBO extends BOBase<PlantaSalao> {
	
	public PlantaSalaoBO() {
        super();
        setClazz(PlantaSalao.class);
    }
	
	public List<PlantaSalao> listarPorEmpresa(Integer idEmpresa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa);
    	return listByFields(parametros);
	}

}
