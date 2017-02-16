package br.com.cardapio.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.cardapio.dao.GenericListDAO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.CryptMD5;

public class MesaBO extends BOBase<Mesa> {
	
	public MesaBO() {
        super();
        setClazz(Mesa.class);
    }
	
	public static void main(String[] args) {
		Empresa find = null;
		try {
			find = new EmpresaBO().find(5);
		} catch (BancoDadosException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i=1;i<31;i++) {
			Mesa mesa = new Mesa();
			
	    	mesa.setEmpresa(find);
	    	mesa.setSenha(""+i);	
	    	mesa.setNumeroMesa(i);
	    	mesa.setDataUltimoFechamento(new Date());
	        try {
				new MesaBO().persist(mesa);
			} catch (BancoDadosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IntegridadeReferencialException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RegistroExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public Mesa buscarPorIdMesaValidandoSenha(Integer idMesa, String senha) throws BancoDadosException{
		Mesa mesa = null;   	
       
    	if(idMesa != null && idMesa.intValue() > 0 && senha != null && !senha.isEmpty()){
    		mesa = find(idMesa);
        	if(!senha.equals(mesa.getSenha()) ){
        		return null; 
        	}
    	}
    	return mesa;
    }
	
	public Mesa buscarPorNumeroMesaValidandoSenha(Integer idEmpresa, Integer numMesa, String senha) throws BancoDadosException{
    	if(numMesa != null && numMesa.intValue() > 0 && senha != null && !senha.isEmpty()){
      		return buscarPorEmpresaENumeroMesaESenha(idEmpresa, numMesa, senha);
    	}
    	return null; 
    }
	
	public Mesa buscarPorEmpresaENumeroMesaESenha(Integer idEmpresa, Integer numMesa, String senha) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("numeroMesa", numMesa);
    	parametros.put("empresa.id", idEmpresa);    	
        parametros.put("senha", senha);
    	return findByFields(parametros);
    }
	
	public Mesa buscarPorEmailEmpresaENumeroMesa(String emailEmpresa, Integer numMesa) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("numeroMesa", numMesa);
    	parametros.put("empresa.email", emailEmpresa);
    	return findByFields(parametros);
    }
	
	public List<Mesa> listarPorEmpresa(Integer idEmpresa) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        String[] ordenacao = new String[]{"numeroMesa"};
        return listByFields(parametros, ordenacao); 
    }
	
	public List<Mesa> listarAtivasPorEmpresa(Integer idEmpresa) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        parametros.put("status", Empresa.STATUS_ATIVO);
        String[] ordenacao = new String[]{"numeroMesa"};
        return listByFields(parametros, ordenacao); 
    }
	
	public List<Mesa> listarAtivasSemPosicaoPorEmpresa(Integer idEmpresa) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        parametros.put("status", Empresa.STATUS_ATIVO);
        parametros.put("o.pontoPlanta is null", GenericListDAO.FILTRO_GENERICO_QUERY);
        String[] ordenacao = new String[]{"numeroMesa"};
        return listByFields(parametros, ordenacao); 
    }
	

    public List<Mesa> listarMesasAbertas(Integer idEmpresa) throws BancoDadosException {
        String jpql = "select distinct(m) from Mesa m,Pedido p where m.empresa.id=:idEmpresa and m.id = p.mesa.id and (p.dataPedido > m.dataUltimoFechamento or m.dataUltimoFechamento is null)";
        Query q = getDao().createQuery(jpql);
        q.setParameter("idEmpresa", idEmpresa);
        return getDao().list(q);
    }
	
	public void fecharMesa(Mesa mesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		mesa.setDataUltimoFechamentoBkp(mesa.getDataUltimoFechamento());
		mesa.setDataUltimoFechamento(new Date());
		mesa.setQtdeMensagensGanhasBkp(mesa.getQtdeMensagensGanhas());
		mesa.setQtdeMensagensGanhas(0);
		mesa.setSenha(Mesa.getSenhaRandom());
		merge(mesa);
	}
	
	public void fecharMesas(Integer idEmpresa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		Empresa empresa = new EmpresaBO().find(idEmpresa);
		List<Mesa> mesas = empresa.getMesas();
		fecharMesas(mesas);
	}
	
	public void fecharMesas(List<Mesa> mesas) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		for(Mesa mesa : mesas){
			mesa.setDataUltimoFechamentoBkp(mesa.getDataUltimoFechamento());
			mesa.setDataUltimoFechamento(new Date());
			mesa.setQtdeMensagensGanhasBkp(mesa.getQtdeMensagensGanhas());
			mesa.setQtdeMensagensGanhas(0);
			mesa.setSenha(Mesa.getSenhaRandom());
		}
		mergeBatch(mesas);
	}
	
	public void fecharMesa(Integer idMesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
		fecharMesa(find(idMesa));
	}
	
	public void reAbrirMesa(Integer idMesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
		fecharMesa(find(idMesa));
	}
	
	public void reAbrirMesa(Mesa mesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		mesa.setDataUltimoFechamento(mesa.getDataUltimoFechamentoBkp());
		mesa.setQtdeMensagensGanhas(mesa.getQtdeMensagensGanhasBkp());
		merge(mesa);
	}
	
	public void reAbrirMesas(List<Mesa> mesas) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		for(Mesa mesa : mesas){
			mesa.setDataUltimoFechamento(mesa.getDataUltimoFechamentoBkp());
			mesa.setQtdeMensagensGanhas(mesa.getQtdeMensagensGanhasBkp());
		}
		mergeBatch(mesas);
	}
	
	public void reAbrirMesas(Integer idEmpresa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		Empresa empresa = new EmpresaBO().find(idEmpresa);
		List<Mesa> mesas = empresa.getMesas();
		reAbrirMesas(mesas);
	}
	
	public void addMensagensPorPedido(Pedido pedido) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
		Mesa mesa = pedido.getMesa();
		int qtdeMsgsAtuais = mesa.getQtdeMensagensGanhas() == null ? 0 : mesa.getQtdeMensagensGanhas();
		qtdeMsgsAtuais += pedido.getQtdeMensagensGanhas();
		mesa.setQtdeMensagensGanhas(qtdeMsgsAtuais);		
		persist(mesa);
	}
	
	public void debitarQtdeMensagemMesa(Mesa mesa) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException {
		if(mesa.isTemCreditoMensagens()){
			Integer qtdeMsgs = mesa.getQtdeMensagensGanhas() - 1;
			mesa.setQtdeMensagensGanhas(qtdeMsgs);
			merge(mesa);
		}
	}

}