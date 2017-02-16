package br.com.cardapio.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Tradutor;
import br.com.cardapio.util.Utils;

public class ProdutoBO extends BOBase<Produto> {
	
	public ProdutoBO() {
		super();
        setClazz(Produto.class);
	}
	
	public void persisteProdutoBatch(HashMap<String, GrupoProduto> hashGP,List<String[]> dados) throws Exception{
        List<Produto> produtos = new ArrayList<Produto>();
        for (String[] s : dados) {
            if (s.length == 8) {
                GrupoProduto grupoProduto = hashGP.get(s[0]);
                Produto p = new Produto(s[2], Utils.split(s[3], 50), Integer.parseInt(s[4]), s[5], Double.parseDouble(s[6].replaceAll(",", ".")),
                        Integer.parseInt(s[7]), false, grupoProduto);
                produtos.add(p);
            } 
        }
        persistBatch(produtos);
    }
	
	public List<Produto> buscarProdutosAtivosPorGrupo(int idGrupoProduto) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("grupoProduto.id", idGrupoProduto);
    	parametros.put("status", Produto.STATUS_ATIVO);
    	String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao); 
	}
	

    public List<Produto> buscarProdutoPorGrupo(int idGrupoProduto) throws BancoDadosException{
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("grupoProduto.id", idGrupoProduto);
        String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao); 
    }
    
    public List<Produto> buscarProdutoPorEmpresa(int idEmpresa) throws BancoDadosException{
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("grupoProduto.empresa.id", idEmpresa);
        String[] ordenacao = new String[]{"ordenacao","nome"};        
        return listByFields(parametros, ordenacao); 
    }
    
    public List<Produto> buscarProdutoPorEmpresa(int idEmpresa, Lingua lingua) throws BancoDadosException{
        List<Produto> produtos = buscarProdutoPorEmpresa(idEmpresa);
        Tradutor.traduzirProdutos(produtos, lingua);
        return produtos; 
    }
    
    public List<Produto> buscarProdutoPorEmpresaETexto(int idEmpresa, String texto) throws BancoDadosException{
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("grupoProduto.empresa.id", idEmpresa);
        parametros.put("status", Produto.STATUS_ATIVO);
        String filtro = "lower(o.nome) like '%" +texto.toLowerCase() + "%'"; 
        parametros.put(filtro, GenericListDAO.FILTRO_GENERICO_QUERY);
        String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao); 
    }
	
	public List<Produto> buscarProdutosEmDestaque() throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("flDestaque", true);
    	parametros.put("status", Produto.STATUS_ATIVO);
    	String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao); 
	}
	
	public List<Produto> buscarProdutosEmDestaqueNoGrupo(int idGrupoProduto) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("grupoProduto.id", idGrupoProduto);
    	parametros.put("flDestaque", true);
    	parametros.put("status", Produto.STATUS_ATIVO);
    	String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao); 
	}
	
	

}
