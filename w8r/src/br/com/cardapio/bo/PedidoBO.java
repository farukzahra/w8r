package br.com.cardapio.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.ProdutoPergunta;
import br.com.cardapio.entity.StatusPedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;

public class PedidoBO extends BOBase<Pedido> {
	
	public PedidoBO() {
        super();
        setClazz(Pedido.class);
    }
	
	public Pedido fazerPedido(Produto produto, Mesa mesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		return fazerPedido(produto, mesa, null, 1);
	}
	
	public Pedido fazerPedido(Produto produto, Mesa mesa, Cliente cliente, int quantidade) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		Pedido pedido = new Pedido();
		pedido.setProduto(produto);
		pedido.setMesa(mesa);
		pedido.setStatusPedido(StatusPedido.AGUARDANDO_ATENDIMENTO);
		pedido.setDataPedido(new Date());
		pedido.setPreco(produto.getPreco());
		pedido.setQuantidade(quantidade <= 0 ? 1 : quantidade);
		pedido.setQtdeMensagensGanhas(produto.getQtdeMensagensDadas() * pedido.getQuantidade());
		pedido.setLocalizacao(mesa.getDescricao());
		if(cliente != null && cliente.getId() != null)
			pedido.setCliente(cliente);
		
		String descricao = "";
		for(ProdutoPergunta prodPergunta : produto.getProdutoPerguntas()){
			if(prodPergunta.getRespostaCliente() != null){
				Pergunta pergunta = prodPergunta.getPergunta();
				Lingua linguaEmpresa = pergunta.getEmpresa().getLinguaPadrao();
				pergunta.setLinguaTraduzida(linguaEmpresa);
				descricao += prodPergunta.getPergunta().getDescricao() + ": " + prodPergunta.getRespostaCliente() + ".\n ";
			}
		}
		pedido.setDescricao(descricao);
		persist(pedido);
		
		return pedido;
	}
	
	public void trocarStatus(Pedido pedido, StatusPedido novoStatus){
		if(novoStatus!= null && novoStatus == pedido.getStatusPedido() ){
			pedido.setStatusPedido(novoStatus);
			pedido.setDataUltimoStatus(new Date());
		}
	}
	
	public List<Pedido> buscarPedidosPorMesa(int idMesa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesa.id", idMesa);
    	parametros.put("o.dataPedido >= o.mesa.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
    	return listByFields(parametros);
	}
	
	public List<Pedido> buscarPedidosEntreguesPorEmpresaEPeriodo(int idEmpresa, Date dtInicial, Date dtFinal) throws BancoDadosException{
    	try{
            String jpql = "select o from Pedido o where " +
        				  " o.mesa.empresa.id = :idEmpresa " +
        				  " and o.dataPedido between :dtInicial and :dtFinal " +
        				  " and o.statusPedido = :status order by o.dataPedido asc";
        	Query query = getDao().createQuery(jpql);
            query.setParameter("idEmpresa", idEmpresa);
            query.setParameter("dtInicial", dtInicial);
            query.setParameter("dtFinal", dtFinal);
            query.setParameter("status", StatusPedido.ENTREGUE);
            return list(query);  
        } catch (Exception e) {
            throw new BancoDadosException(e);
        }
	}
	
	public List<Pedido> buscarPedidosPorEmpresaEPeriodo(int idEmpresa, Date dtInicial, Date dtFinal) throws BancoDadosException{
    	try{
            String jpql = "select o from Pedido o where " +
        				  " o.mesa.empresa.id = :idEmpresa " +
        				  " and o.dataPedido between :dtInicial and :dtFinal " +
        				  " and o.statusPedido = :status ";
        	Query query = getDao().createQuery(jpql);
            query.setParameter("idEmpresa", idEmpresa);
            query.setParameter("dtInicial", dtInicial);
            query.setParameter("dtFinal", dtFinal);
            return list(query);  
        } catch (Exception e) {
            throw new BancoDadosException(e);
        }
	}
	
	public List<Pedido> buscarPedidosEntreguesPorMesa(int idMesa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesa.id", idMesa);
    	parametros.put("o.dataPedido >= o.mesa.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
    	parametros.put("statusPedido", StatusPedido.ENTREGUE);
    	return listByFields(parametros);
	}
	
	public List<Pedido> buscarPedidosNaoEntreguesPorMesa(int idMesa) throws BancoDadosException{
        try{
            String jpql = "select o from Pedido o where " +
                          " o.mesa.id = :idMesa and o.dataPedido >=  o.mesa.dataUltimoFechamento" +
                          " and o.statusPedido <> :status ";
            Query query = getDao().createQuery(jpql);
            query.setParameter("idMesa", idMesa);
            query.setParameter("status", StatusPedido.ENTREGUE);
            return list(query);  
        } catch (Exception e) {
            throw new BancoDadosException(e);
        }
    }
	
	public List<Pedido> buscarPedidosPorEmpresa(int idEmpresa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesa.empresa.id", idEmpresa);
    	return listByFields(parametros);
	}
	
	public List<Pedido> buscarPedidosPorMesaEGrupoProduto(int idMesa, int idGrupoProduto) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesa.id", idMesa);
    	parametros.put("produto.grupoProduto.id", idMesa);
    	parametros.put("o.dataPedido >= o.mesa.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
    	return listByFields(parametros);
	}

	public List<Pedido> buscarPedidosPorStatus(StatusPedido statusPedido, int idEmpresa) throws BancoDadosException{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("statusPedido", statusPedido);
    	return listByFields(parametros);
	}
	
	public List<Pedido> buscarPedidosPorStatus(List<StatusPedido> statusPedido, int idEmpresa, String filtroProduto) throws BancoDadosException{
		if(statusPedido != null && !statusPedido.isEmpty()){
	    	
	    	try{
	    	    String filtroProdutoSql = "";
	    	    if(filtroProduto != null && !filtroProduto.isEmpty())
	    	        filtroProdutoSql = " and o.produto.nome like '"+filtroProduto+"%' ";
	            String jpql = "select o from Pedido o where " +
	        				  " o.mesa.empresa.id = :idEmpresa " + filtroProdutoSql +
	        				  " and o.dataPedido >= o.mesa.dataUltimoFechamento" +
	        				  " and o.statusPedido in (";
	            int i=0;
	            for(StatusPedido sp : statusPedido){
	            	jpql += " ,:sp"+i++;
	            }
	            jpql = jpql.replaceFirst(",", " ");
	            jpql += ") order by o.dataPedido asc, o.mesa.numeroMesa";
	        	Query query = getDao().createQuery(jpql);
	            query.setParameter("idEmpresa", idEmpresa);
	            
	            i=0;
	            for(StatusPedido sp : statusPedido){
	            	query.setParameter("sp"+i++, sp);
	            }
	            
	            return list(query);  
	        } catch (Exception e) {
	            throw new BancoDadosException(e);
	        }
		}else
			return null;
		
	}

}
