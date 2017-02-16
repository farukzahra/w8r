package br.com.cardapio.entity;

import java.util.Locale;

public enum Lingua {
    PT("Português",new Locale("pt","BR") ),
    EN("English",Locale.US),
    ES("Español",new Locale("es","ES")),
    IT("Italiano",Locale.ITALY),
    FR("Française",Locale.FRANCE),
    DE("Deutsch",Locale.GERMANY);
    
    private String label;
    
    private Locale locale;
    
    private Lingua(String label, Locale locale){
        this.label = label;
        this.locale = locale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    
}