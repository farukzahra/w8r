package br.com.cardapio.exception;

import br.com.cardapio.util.Mensagens;


public class UsuarioNaoEncontradoException extends BusinessException {
    
    private static String mensagem = Mensagens.getMensagem(Mensagens.EXCEPTION_USUARIO_NAO_ENCONTRADO);

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    
}
