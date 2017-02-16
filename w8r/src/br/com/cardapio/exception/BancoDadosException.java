package br.com.cardapio.exception;

public class BancoDadosException extends TechnicalException {
    
    public BancoDadosException(Exception e) {    
        super(e);
    }
}
