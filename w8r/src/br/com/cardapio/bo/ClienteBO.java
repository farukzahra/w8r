package br.com.cardapio.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.Cliente;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.LoginExpiradoException;
import br.com.cardapio.exception.SenhaInvalidaException;
import br.com.cardapio.util.CryptMD5;

public class ClienteBO extends BOBase<Cliente> {
	
	public ClienteBO() {
        super();
        setClazz(Cliente.class);
    }

	public Cliente findByEmail(String email) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("email", email); 
        return findByFields(parametros);
    }
	
	public Cliente buscarPorLoginValidandoSenha(Integer idEmpresa, String login, String senha) throws BancoDadosException, SenhaInvalidaException, LoginExpiradoException{
		Cliente cliente = null;    	
        
    	if(login != null && !login.isEmpty() && senha != null && !senha.isEmpty()){
    		cliente = buscarPorEmpresaELoginESenha(idEmpresa, login, CryptMD5.encrypt(senha));
        	if(cliente == null ){
        		throw new SenhaInvalidaException(); 
        	}
        	if(cliente.getDataExpiracaoLogin().before(new Date()))
        		throw new LoginExpiradoException();
    	}
    	return cliente;
    }
	
	public Cliente buscarPorEmpresaELoginESenha(Integer idEmpresa, String login, String senha) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("login", login);
    	parametros.put("empresa.id", idEmpresa);    	
        parametros.put("senha", senha);
    	return findByFields(parametros);
    }
	
	public List<Cliente> listarPorEmpresa(Integer idEmpresa) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa); 
    	String[] sort = new String[]{"nome"};
    	return listByFields(parametros, sort);
    }
	
	public List<Cliente> listarPorEmpresaEDocumento(Integer idEmpresa, String documento) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa); 
    	parametros.put("documento", documento); 
    	String[] sort = new String[]{"nome"};
    	return listByFields(parametros, sort);
    }
	
	public List<Cliente> listarPorEmpresaETelefone(Integer idEmpresa, String telefone) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("empresa.id", idEmpresa); 
    	parametros.put("telefone", telefone); 
    	String[] sort = new String[]{"nome"};
    	return listByFields(parametros, sort);
    }
}
