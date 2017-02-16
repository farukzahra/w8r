package br.com.cardapio.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the MENSAGEM database table.
 * 
 */
@Entity
@Table(name="MENSAGEM")
public class Mensagem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_MENSAGEM", unique=true, nullable=false)
	private Long id;

	@Column(name="DS_MENSAGEM")
	private String descricao;

	@Column(name="FL_LEITURA")
	private Boolean flLeitura;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_ENVIO")
	private Date dataEnvio;

	//bi-directional many-to-one association to Mesa
    @ManyToOne
	@JoinColumn(name="ID_MESA_ORIGEM")
	private Mesa mesaOrigem;

	//bi-directional many-to-one association to Mesa
    @ManyToOne
	@JoinColumn(name="ID_MESA_DESTINO")
	private Mesa mesaDestino;

    public Mensagem() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long idMensagem) {
		this.id = idMensagem;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descMensagem) {
		this.descricao = descMensagem;
	}

	public Boolean isFlLeitura() {
		return this.flLeitura;
	}

	public void setFlLeitura(Boolean flLeitura) {
		this.flLeitura = flLeitura;
	}

	public Mesa getMesaOrigem() {
		return this.mesaOrigem;
	}

	public void setMesaOrigem(Mesa mesaOrigem) {
		this.mesaOrigem = mesaOrigem;
	}
	
	public Mesa getMesaDestino() {
		return this.mesaDestino;
	}

	public void setMesaDestino(Mesa mesaDestino) {
		this.mesaDestino = mesaDestino;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
	
	public String getHoraEnvio(){
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    return sdf.format(dataEnvio);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Mensagem other = (Mensagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}