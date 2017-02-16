package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;

import br.com.cardapio.bo.ClienteBO;
import br.com.cardapio.entity.Cliente;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.CryptMD5;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class ClienteMB extends BaseManagedBean<Cliente> {
    private static final long serialVersionUID = 1L;

    public ClienteMB() {
        this.setClazz(Cliente.class);
        try {
			setEntityList(new ClienteBO().listarPorEmpresa(getEmpresaLogada().getId()));
			Cliente cliente = new Cliente();
			cliente.setEmpresa(getEmpresaLogada());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, 24);
			cliente.setDataExpiracaoLogin(c.getTime());
			setEntity(cliente);
		} catch (BancoDadosException e) {
			addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
		}
    }
    
    @Override
    public void actionPersist(ActionEvent event) {
    	getEntity().setEmpresa(getEmpresaReferencia());
    	if(getEntity().getSenha() != null){
	    	String senha = CryptMD5.encrypt(getEntity().getSenha());
	    	getEntity().setSenha(senha);
    	}
    	super.actionPersist(event);
    }
    
    public void handleSelect(SelectEvent event) {  
    	Cliente cliente = (Cliente)event.getObject(); 
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, 24);
		cliente.setDataExpiracaoLogin(c.getTime());
    	setEntity(cliente);
    }
    
    public List<Cliente> completaCliente(String query) {  
        List<Cliente> clientes = getEntityList();
        List<Cliente> clientesSel = new ArrayList<Cliente>();  
          
        for(Cliente cliente : clientes) {  
            if((cliente.getLogin() != null && cliente.getLogin().toLowerCase().contains(query.toLowerCase()))
            		|| (cliente.getNome() != null && cliente.getNome().toLowerCase().contains(query.toLowerCase())))  
            	clientesSel.add(cliente);  
        }  
          
        return clientesSel;  
    }

    

    
}
