package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.bo.UsuarioBO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Usuario;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class LoginEmpresaMB extends BaseManagedBean<Empresa> {

    private static final long serialVersionUID = 1L;
    
    public LoginEmpresaMB() {
        setClazz(Empresa.class);        
        setEmpresaLogada(null);
    }

    public String actionLoginEmpresa(){
    	Empresa empresaLogando = new EmpresaBO().findByEmailValidandoSenha(getEntity().getEmail(), getEntity().getSenha());
    	if(empresaLogando != null){          
    		setEmpresaLogada(empresaLogando);  
    		setMesaLogada(null);
    		setClienteLogado(null);
    		JSFHelper.redirect("listapedido.jsf?faces-redirect=true");
    		//return "principalempresa.xhtml";
    	}else{
    	    Usuario usuario = new UsuarioBO().findByLoginValidandoSenha(getEntity().getEmail(), getEntity().getSenha());
    	    if(usuario != null){
    	        usuario.getEmpresa().setPerfil(usuario.getPerfil());
    	        setEmpresaLogada(usuario.getEmpresa());  
                setMesaLogada(null);
                setClienteLogado(null);
                JSFHelper.redirect("listapedido.jsf?faces-redirect=true");
    	    }else{
    	        addError(Mensagens.getMensagem(Mensagens.USUARIO_SENHA_INVALIDO));    
    	    }    		
    	}
    	return "loginempresa.xhtml";
    }
    
    public String actionLogoff(){
        setEmpresaLogada(null);
        setMesaLogada(null);
        return "loginempresa.jsf?faces-redirect=true";
    }
    
    public static void main(String[] args) {
        Empresa empresaLogando = new EmpresaBO().findByEmailValidandoSenha("matriz","matriz");
        System.out.println(empresaLogando);
    }
    
}