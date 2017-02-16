package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.GrupoProdutoTraducao;
import br.com.cardapio.exception.BancoDadosException;

public class GrupoProdutoTraducaoBO extends BOBase<GrupoProdutoTraducao> {
    
    private static final long serialVersionUID = 1L;

    public GrupoProdutoTraducaoBO() {
        super();
        setClazz(GrupoProdutoTraducao.class);
    }
    
    public List<GrupoProdutoTraducao> listarPorGrupo(Integer idGrupo) throws BancoDadosException{  
    	Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("grupoProduto.id", idGrupo);
        return listByFields(parametros);       
    }

    
}