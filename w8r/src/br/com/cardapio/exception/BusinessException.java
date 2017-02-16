package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;

public class BusinessException extends Exception{

    private String mensagem = Mensagens.getMensagem(Mensagens.EXCEPTION_BUSINESS);

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public BusinessException(Throwable cause, String mensagem) {
        super(cause);
        this.mensagem = mensagem;
    }
    
    public BusinessException(String mensagem) {
        super();
        this.mensagem = mensagem;
    }
    
    public BusinessException() {
        super();
    }
}
