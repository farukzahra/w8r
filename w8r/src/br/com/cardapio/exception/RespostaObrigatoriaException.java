package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;

public class RespostaObrigatoriaException extends BusinessException {
	
	public RespostaObrigatoriaException(){
        super(Mensagens.getMensagem(Mensagens.EXCEPTION_CAMPO_OBRIGATORIO));
    }
    
    public RespostaObrigatoriaException(Throwable cause){
        super(cause, Mensagens.getMensagem(Mensagens.EXCEPTION_CAMPO_OBRIGATORIO));
    }

}
