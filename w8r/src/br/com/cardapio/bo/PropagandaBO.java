package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.Propaganda;
import br.com.cardapio.exception.BancoDadosException;

public class PropagandaBO extends BOBase<Propaganda> {
    public PropagandaBO() {
        super();
        setClazz(Propaganda.class);
    }

    public List<Propaganda> listByEmpresa(int idEmpresa) throws BancoDadosException{
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        return listByFields(parametros);
    }
}
