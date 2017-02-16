package br.com.cardapio.managed;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;

@ManagedBean
@SessionScoped
public class SessionManaged implements Serializable {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;

	private Mesa mesa;
	
	private Cliente cliente;
	
	private Lingua lingua;
	
	private String tema = "b";

	public SessionManaged() {
		System.out.println("Criando Session!");
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}
	
}
