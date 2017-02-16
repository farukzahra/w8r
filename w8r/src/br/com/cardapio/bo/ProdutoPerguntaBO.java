package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.ProdutoPergunta;
import br.com.cardapio.exception.BancoDadosException;

public class ProdutoPerguntaBO extends BOBase<ProdutoPergunta> {
	
	private static final long serialVersionUID = 1L;

	public ProdutoPerguntaBO() {
		super();
		setClazz(ProdutoPergunta.class);
	}
	
	public void removerPerguntas(int idProduto) throws BancoDadosException{
		String jpql = "delete from ProdutoPergunta o where o.produto.id = :idProduto ";
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("idProduto", idProduto);
    	executeQuery(jpql, parametros);    	
	}
	
	public ProdutoPergunta buscarPorProdutoEPergunta(int idProduto, int idPergunta) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("produto.id", idProduto);
    	parametros.put("pergunta.id", idPergunta);
    	return findByFields(parametros);
	}
	
	public List<ProdutoPergunta> listarPorProduto(int idProduto) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("produto.id", idProduto);
    	return listByFields(parametros);
	}
}
