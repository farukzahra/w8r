package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the MESA database table.
 */
@Entity
@Table(name = "MESA")
public class Mesa implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Character STATUS_ATIVO = 'A';
	public static final Character STATUS_INATIVO = 'I';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_MESA", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DS_MESA")
	private String descricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ULTIMO_FECHAMENTO")
	private Date dataUltimoFechamento;

	@Column(name = "NR_MESA")
	private Integer numeroMesa;

	@Column(name = "TX_SENHA", nullable = false)
	private String senha;

	@Column(name = "IN_STATUS")
	private Character status = STATUS_ATIVO;

	@Column(name = "QT_MENSAGENS_GANHAS")
	private Integer qtdeMensagensGanhas;

	@ManyToOne
	@JoinColumn(name = "ID_EMPRESA")
	private Empresa empresa;

	@OneToMany(mappedBy = "mesaOrigem")
	private List<Mensagem> mensagensEnviadas;

	@OneToMany(mappedBy = "mesaDestino")
	private List<Mensagem> mensagensRecebidas;

	@OneToMany(mappedBy = "mesa")
	private List<Pedido> pedidos;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ULT_FECHAMENTO_BKP")
	private Date dataUltimoFechamentoBkp;

	@Column(name = "QT_MSGS_GANHAS_BKP")
	private Integer qtdeMensagensGanhasBkp;
	
	@Column(name = "QT_LARGURA")
	private Integer largura;

	@Column(name = "QT_COMPRIMENTO")
	private Integer comprimento;
	
	@ManyToOne
	@JoinColumn(name = "ID_PLANTA_SALAO")
	private PlantaSalao plantaSalao;
	
	@Column(name = "nr_ponto_planta")
	private Integer pontoPlanta;

	@Transient
	private boolean flEnviouNovaMsg;

	public Mesa(Integer numeroMesa, String senha, Empresa empresa) {
		this.numeroMesa = numeroMesa;
		this.senha = senha;
		this.empresa = empresa;
	}

	public Mesa() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idMesa) {
		this.id = idMesa;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String DescMesa) {
		this.descricao = DescMesa;
	}

	public Date getDataUltimoFechamento() {
		return this.dataUltimoFechamento;
	}

	public void setDataUltimoFechamento(Date dtUltimoFechamento) {
		this.dataUltimoFechamento = dtUltimoFechamento;
	}

	public Integer getNumeroMesa() {
		return this.numeroMesa;
	}

	public void setNumeroMesa(Integer numeroMesa) {
		this.numeroMesa = numeroMesa;
	}

	public List<Mensagem> getMensagensEnviadas() {
		return this.mensagensEnviadas;
	}

	public void setMensagensEnviadas(List<Mensagem> mensagensEnviadas) {
		this.mensagensEnviadas = mensagensEnviadas;
	}

	public List<Mensagem> getMensagensRecebidas() {
		return this.mensagensRecebidas;
	}

	public void setMensagensRecebidas(List<Mensagem> mensagensRecebidas) {
		this.mensagensRecebidas = mensagensRecebidas;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Pedido> getPedidos() {
		return this.pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getQtdeMensagensGanhas() {
		return qtdeMensagensGanhas;
	}

	public void setQtdeMensagensGanhas(Integer qtdeMensagensGanhas) {
		this.qtdeMensagensGanhas = qtdeMensagensGanhas;
	}

	public boolean isTemCreditoMensagens() {
		return (this.qtdeMensagensGanhas != null && this.qtdeMensagensGanhas > 0);
	}

	public Character getStatusAtivo() {
		return STATUS_ATIVO;
	}

	public Character getStatusInativo() {
		return STATUS_INATIVO;
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
		Mesa other = (Mesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isFlEnviouNovaMsg() {
		return flEnviouNovaMsg;
	}

	public void setFlEnviouNovaMsg(boolean flEnviouMensagem) {
		this.flEnviouNovaMsg = flEnviouMensagem;
	}

	public boolean isFlRecebeuNovaMsg() {
		for (Mensagem msgRecebida : this.getMensagensRecebidas()) {
			if (msgRecebida.isFlLeitura() == false
					&& (msgRecebida.getDataEnvio().after(
							this.getDataUltimoFechamento()) || this
							.getDataUltimoFechamento() == null)) {
				return true;
			}
		}
		return false;
	}

	public Date getDataUltimoFechamentoBkp() {
		return dataUltimoFechamentoBkp;
	}

	public void setDataUltimoFechamentoBkp(Date dataUltimoFechamentoBkp) {
		this.dataUltimoFechamentoBkp = dataUltimoFechamentoBkp;
	}

	public Integer getQtdeMensagensGanhasBkp() {
		return qtdeMensagensGanhasBkp;
	}

	public void setQtdeMensagensGanhasBkp(Integer qtdeMensagensGanhasBkp) {
		this.qtdeMensagensGanhasBkp = qtdeMensagensGanhasBkp;
	}

	public PlantaSalao getPlantaSalao() {
		return plantaSalao;
	}

	public void setPlantaSalao(PlantaSalao plantaSalao) {
		this.plantaSalao = plantaSalao;
	}

	public Integer getLargura() {
		return largura;
	}

	public void setLargura(Integer largura) {
		this.largura = largura;
	}

	public Integer getComprimentoCm() {
		return comprimento;
	}

	public void setComprimento(Integer comprimento) {
		this.comprimento = comprimento;
	}

	public Integer getPontoPlanta() {
		return pontoPlanta;
	}

	public void setPontoPlanta(Integer pontoPlanta) {
		this.pontoPlanta = pontoPlanta;
	}

	public Integer getComprimento() {
		return comprimento;
	}
	
    public static String getSenhaRandom() {
        Random gerador = new Random();
        return gerador.nextInt(9999) + "";
    }

}
