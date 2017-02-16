package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the EMPRESA database table.
 */
@Entity
@Table(name = "EMPRESA")
public class Empresa implements Serializable, Comparable<Empresa> {
	private static final long serialVersionUID = 1L;

	public static final char STATUS_ATIVO = 'A';
	public static final char STATUS_INATIVO = 'I';
	
	public static final Character PERFIL_ADMINISTRADOR = 'A';
	public static final Character PERFIL_NORMAL = 'N';
	public static final Character PERFIL_COZINHA = 'C';	
	
	public static final Character TIPO_LOGIN_MESA = 'M';
	public static final Character TIPO_LOGIN_CLIENTE = 'C';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_EMPRESA", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DS_EMPRESA")
	private String descricao = "";

	@Column(name = "NM_EMPRESA")
	private String nome = "";

	@Column(name = "IN_STATUS")
	private Character status = STATUS_ATIVO;

	@Column(name = "TX_EMAIL", unique = true, nullable = false)
	private String email = "";

	@Column(name = "TX_SENHA", nullable = false)
	private String senha = "";

	@OneToMany(mappedBy = "empresa")
	private List<Mesa> mesas = new ArrayList<Mesa>();

	@OneToMany(mappedBy = "empresa")
	private List<Pergunta> perguntas = new ArrayList<Pergunta>();

	@OneToMany(mappedBy = "empresa")
	private List<GrupoProduto> grupoProdutos = new ArrayList<GrupoProduto>();

	@Column(name = "IN_PERFIL", nullable = true)
	private Character perfil = PERFIL_NORMAL;

	@Column(name = "VL_ONDEMAND", precision = 9, scale = 2)
	private Double valorOnDemand;

	@Enumerated(EnumType.STRING)
	@Column(name = "TX_LINGUA_PADRAO")
	private Lingua linguaPadrao = Lingua.PT;

	@Column(name = "FL_MOD_MENSAGEM")
	private boolean flModuloMensagem = false;

	@Column(name = "FL_MOD_PEDIDO")
	private boolean flModuloPedido = false;

	@Column(name = "FL_MOD_IDIOMAS")
	private boolean flModuloIdiomas = false;

	@Column(name = "FL_MOD_SUPORTE")
	private boolean flModuloSuporte = false;

	@Column(name = "TX_SITE")
	private String site;

	@Column(name = "VL_LATITUDE", precision = 5, scale = 2)
	private Double latitude;

	@Column(name = "VL_LONGITUDE", precision = 5, scale = 2)
	private Double longitude;

	@Column(name = "IN_TEMA")
	private String tema = "b";

	@Transient
	private Double distancia;

	@Column(name = "TP_METALOCAL")
	private String metaLocal = MetaLocal.MESA;

	@Column(name = "TX_CNPJ")
	private String cnpj = "";

	@Column(name = "IN_TIPO_LOGIN")
	private Character tipoLogin = TIPO_LOGIN_MESA;

	@Column(name = "TX_ENDERECO")
	private String endereco = "";

	@Column(name = "TX_CIDADE")
	private String cidade = "";

	@Column(name = "IN_UF")
	private String uf = "";
	
	
	public Empresa() {
	}

	public Empresa(String nome, String descricao, String email, String senha, boolean m, boolean p, boolean s, boolean i) {
		this.descricao = descricao;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.flModuloMensagem = m;
		this.flModuloPedido = p;
		this.flModuloSuporte = s;
		this.flModuloIdiomas = i;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idEmpresa) {
		this.id = idEmpresa;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descEmpresa) {
		this.descricao = descEmpresa;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nomeEmpresa) {
		this.nome = nomeEmpresa;
	}

	public List<Mesa> getMesas() {
		return this.mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;// CryptMD5.encrypt(senha);
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	public List<GrupoProduto> getGrupoProdutos() {
		return grupoProdutos;
	}

	public void setGrupoProdutos(List<GrupoProduto> grupoProdutos) {
		this.grupoProdutos = grupoProdutos;
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Character getPerfil() {
		return perfil;
	}

	public void setPerfil(Character perfil) {
		this.perfil = perfil;
	}

	public boolean isAdministrador() {
		return perfil == PERFIL_ADMINISTRADOR;
	}

	public Character getPerfilAdministrador() {
		return PERFIL_ADMINISTRADOR;
	}

	public Character getPerfilNormal() {
		return PERFIL_NORMAL;
	}
	
	public Character getPerfilCozinha() {
        return PERFIL_COZINHA;
    }

	public Double getValorOnDemand() {
		return valorOnDemand;
	}

	public void setValorOnDemand(Double valorOnDemand) {
		this.valorOnDemand = valorOnDemand;
	}

	public static void main(String[] args) {
		Character c = null;
		if (c == 'A') {
			System.out.println("1");
		} else {
			System.out.println("2");
		}
	}

	public Lingua getLinguaPadrao() {
		return linguaPadrao;
	}

	public void setLinguaPadrao(Lingua linguaPadrao) {
		this.linguaPadrao = linguaPadrao;
	}

	public boolean isFlModuloMensagem() {
		return flModuloMensagem;
	}

	public void setFlModuloMensagem(boolean flModuloMensagem) {
		this.flModuloMensagem = flModuloMensagem;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public Double getDistancia() {
		return distancia;
	}

	public int getDistanciaMetros() {
		return distancia != null ? ((Double) (distancia * 100000)).intValue() : 0;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public String getMetaLocal() {
		return metaLocal;
	}

	public void setMetaLocal(String metaLocal) {
		this.metaLocal = metaLocal;
	}

	@Override
	public int compareTo(Empresa o) {
		return this.getDistancia().compareTo(o.getDistancia());
	}

	public boolean isFlModuloPedido() {
		return flModuloPedido;
	}

	public void setFlModuloPedido(boolean flModuloPedido) {
		this.flModuloPedido = flModuloPedido;
	}

	public boolean isFlModuloIdiomas() {
		return flModuloIdiomas;
	}

	public void setFlModuloIdiomas(boolean flModuloIdiomas) {
		this.flModuloIdiomas = flModuloIdiomas;
	}

	public boolean isFlModuloSuporte() {
		return flModuloSuporte;
	}

	public void setFlModuloSuporte(boolean flModuloSuporte) {
		this.flModuloSuporte = flModuloSuporte;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Character getTipoLogin() {
		return tipoLogin;
	}

	public void setTipoLogin(Character tipoLogin) {
		this.tipoLogin = tipoLogin;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public boolean isTipoLoginCliente() {
		return (TIPO_LOGIN_CLIENTE.equals(tipoLogin)) ? true : false;
	}

	public boolean isTipoLoginMesa() {
		return (TIPO_LOGIN_MESA.equals(tipoLogin)) ? true : false;
	}

}
