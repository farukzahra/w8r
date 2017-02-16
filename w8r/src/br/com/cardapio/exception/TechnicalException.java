package br.com.cardapio.exception;

public class TechnicalException extends Exception {
    
    public TechnicalException(Exception e) {    
        super(e);
    }
}
