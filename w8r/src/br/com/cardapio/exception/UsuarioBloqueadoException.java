package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;


public class UsuarioBloqueadoException extends BusinessException{
    
    private String mensagem = Mensagens.getMensagem(Mensagens.EXCEPTION_USUARIO_BLOQUEADO);

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
