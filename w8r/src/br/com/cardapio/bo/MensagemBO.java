package br.com.cardapio.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.Mensagem;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.CreditosInsuficException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;

public class MensagemBO extends BOBase<Mensagem> {
	
	private static final long serialVersionUID = 1L;

    public MensagemBO() {
        super();
        setClazz(Mensagem.class);
    }
    
    public void flegarLeitura(Long idMensagem) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
    	Mensagem mensagem = find(idMensagem);
    	flegarLeitura(mensagem);
    }
    
    public void flegarLeitura(Mensagem mensagem) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
        mensagem.setFlLeitura(true);
        persist(mensagem);
    }
    
    public void flegarLeituraBatch(List<Mensagem> dialogo, Integer idMesaLeitora) throws BancoDadosException, IntegridadeReferencialException{    	
    	if(dialogo != null){
    		List<Mensagem> mensagensNovas = new ArrayList<Mensagem>();
	    	for(Mensagem mensagem : dialogo){
	    		if(idMesaLeitora == mensagem.getMesaDestino().getId() && mensagem.isFlLeitura() == false){
	    			mensagem.setFlLeitura(true);
	    			mensagensNovas.add(mensagem);
	    		}    		
	    	}      
	    	if(!mensagensNovas.isEmpty())
	    		mergeBatch(mensagensNovas);
    	}
    }
    
    public void flegarLeituraPorMesa(Mensagem mensagem) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
        mensagem.setFlLeitura(true);
        persist(mensagem);
    }
    
    public List<Mensagem> buscarEnviadasPorMesa(int idMesa) throws BancoDadosException{       
    	Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesaOrigem.id", idMesa);
    	parametros.put("o.dataEnvio >= o.mesaOrigem.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
    	return listByFields(parametros);     
    }
    
    public List<Mensagem> buscarRecebidasPorMesa(int idMesa) throws BancoDadosException{       
    	Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("mesaDestino.id", idMesa);
    	parametros.put("o.dataEnvio >= o.mesaDestino.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
    	return listByFields(parametros);     
    }
    
    public List<Mensagem> buscarRecebidasNaoLidasPorMesa(int idMesa) throws BancoDadosException{       
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mesaDestino.id", idMesa);
        parametros.put("flLeitura", false);
        parametros.put("o.dataEnvio >= o.mesaDestino.dataUltimoFechamento", GenericListDAO.FILTRO_GENERICO_QUERY);
        return listByFields(parametros);     
    }
    
    public List<Mensagem> buscarDialogoMesas(int idMesa1, int idMesa2) throws BancoDadosException{       
    	try{
            String jpql = "select o from Mensagem o where " +
        				  "((o.mesaOrigem.id = :idMesa1 and o.mesaDestino.id = :idMesa2) " +
        				  " or (o.mesaOrigem.id = :idMesa2 and o.mesaDestino.id = :idMesa1)) " +
        				  " and (o.dataEnvio >= o.mesaOrigem.dataUltimoFechamento or o.mesaOrigem.dataUltimoFechamento is null)" +
        				  " and (o.dataEnvio >= o.mesaDestino.dataUltimoFechamento or o.mesaDestino.dataUltimoFechamento is null)" +
        				  " order by o.dataEnvio desc";
        	Query query = getDao().createQuery(jpql);
            query.setParameter("idMesa1", idMesa1);
            query.setParameter("idMesa2", idMesa2);
            return list(query);  
        } catch (Exception e) {
            throw new BancoDadosException(e);
        }
    }
    
    public void enviarMensagem(Mesa mesaOrigem, Mesa mesaDestino, String texto) throws CreditosInsuficException, BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
    	if(!mesaOrigem.isTemCreditoMensagens()){
    		throw new CreditosInsuficException();
    	}else{
    		Mensagem mensagem = new Mensagem();
    		mensagem.setDataEnvio(new Date());
    		mensagem.setDescricao(texto);
    		mensagem.setFlLeitura(false);
    		mensagem.setMesaDestino(mesaDestino);
    		mensagem.setMesaOrigem(mesaOrigem);
    		persist(mensagem);
    		new MesaBO().debitarQtdeMensagemMesa(mesaOrigem);
    	}
    }

}
