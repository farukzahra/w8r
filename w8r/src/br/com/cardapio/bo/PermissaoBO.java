package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.Permissao;
import br.com.cardapio.exception.BancoDadosException;

public class PermissaoBO extends BOBase<Permissao> {
	
	public PermissaoBO() {
        super();
        setClazz(Permissao.class);
    }

	public Permissao findUltimaByClienteEmpresa(Integer idCliente, Integer idEmpresa) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("cliente.id", idCliente);
        parametros.put("empresa.id", idEmpresa);
        String[] ordem = new String[]{"data desc"};
        List<Permissao> permissoes = listByFields(parametros, ordem);
        if(permissoes != null && !permissoes.isEmpty()){
        	return permissoes.get(0);
        }else
        	return null;
    }
	
	public List<Permissao> listByEmpresa(Integer idEmpresa) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mesa.empresa.id", idEmpresa); 
        String[] ordem = new String[]{"data"};
        return listByFields(parametros, ordem);
    }
	
	public List<Permissao> listByEmpresaEAbertas(Integer idEmpresa) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mesa.empresa.id", idEmpresa); 
        parametros.put("o.data >= o.mesa.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
        parametros.put("flAprovado", false); 
        String[] ordem = new String[]{"data desc"};
        return listByFields(parametros, ordem);
    }
}
