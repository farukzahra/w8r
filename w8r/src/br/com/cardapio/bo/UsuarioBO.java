package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.Map;

import br.com.cardapio.entity.Usuario;
import br.com.cardapio.exception.BancoDadosException;

public class UsuarioBO extends BOBase<Usuario> {
    private static final long serialVersionUID = 1L;

    public UsuarioBO() {
        super();
        setClazz(Usuario.class);
    }
    
    public Usuario findByLoginValidandoSenha(String login, String senha){
        Usuario usuario = null;     
        try {
            if(login != null && !login.isEmpty() && senha != null && !senha.isEmpty()){
                usuario = findByLoginESenha(login,senha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return usuario;
    }
    
    public Usuario findByLoginESenha(String login, String senha) throws BancoDadosException {        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("login", login);                   
        parametros.put("senha", senha);
        return findByFields(parametros);
    }
    
    public static void main(String[] args) {
        
    }
}
