package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;

public class CreditosInsuficException extends BusinessException {
    public CreditosInsuficException() {
        super(Mensagens.getMensagem(Mensagens.EXCEPTION_CREDITOS_INSUFICIENTE));
    }

    public CreditosInsuficException(
            Throwable cause) {
        super(cause, Mensagens.getMensagem(Mensagens.EXCEPTION_CREDITOS_INSUFICIENTE));
    }
}
