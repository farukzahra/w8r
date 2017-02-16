package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;


public class LoginExpiradoException extends BusinessException{
    
    private String mensagem = Mensagens.getMensagem("cm.exception.login.expirado");

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
