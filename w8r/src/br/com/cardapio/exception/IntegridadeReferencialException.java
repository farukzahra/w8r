package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;

public class IntegridadeReferencialException extends BusinessException {
    
    public IntegridadeReferencialException(){
        super(Mensagens.getMensagem(Mensagens.EXCEPTION_INTEGRIDADE_VIOLADA));
    }
    
    public IntegridadeReferencialException(Throwable cause){
        super(cause, Mensagens.getMensagem(Mensagens.EXCEPTION_INTEGRIDADE_VIOLADA));
    }
    
}
