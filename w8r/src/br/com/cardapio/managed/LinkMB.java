package br.com.cardapio.managed;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class LinkMB implements Serializable{
    
    public String redirect(){
        return "grupoproduto.jsf";
    }
}
