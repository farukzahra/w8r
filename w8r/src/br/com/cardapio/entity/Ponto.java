package br.com.cardapio.entity;

import java.io.Serializable;

public class Ponto implements Serializable, Comparable<Ponto>{
	
	private int x;
	private int y;
	private Integer numeroPonto;
	private Mesa mesa;
	
	public Ponto() {		
	}

	public Ponto(int x, int y, int numeroPonto) {
		super();
		this.x = x;
		this.y = y;
		this.numeroPonto = numeroPonto;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numeroPonto;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ponto other = (Ponto) obj;
		if (numeroPonto != other.numeroPonto)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public Integer getNumeroPonto() {
		return numeroPonto;
	}

	public void setNumeroPonto(Integer numeroPonto) {
		this.numeroPonto = numeroPonto;
	}

	@Override
	public int compareTo(Ponto o) {
		return this.numeroPonto.compareTo(o.getNumeroPonto());
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

}
