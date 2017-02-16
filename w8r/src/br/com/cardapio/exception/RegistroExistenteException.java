package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;

public class RegistroExistenteException extends BusinessException {
    
    public RegistroExistenteException(){
        super(Mensagens.getMensagem(Mensagens.EXCEPTION_REGISTRO_EXISTENTE));
    }
    
    public RegistroExistenteException(Throwable cause){
        super(cause, Mensagens.getMensagem(Mensagens.EXCEPTION_REGISTRO_EXISTENTE));
    }
    
}
