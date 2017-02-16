package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

@Entity
@Table(name = "CLIENTE")
public class Cliente implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "TX_LOGIN")
	private String login;
	
	@Column(name = "TX_SENHA")
	private String senha;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_EXPIRACAO_LOGIN")
	private Date dataExpiracaoLogin;
	
	@Column(name = "TX_DOCUMENTO")
	private String documento;
	
	@Column(name = "TX_EMAIL")
	private String email;
	
	@Column(name = "TX_NOME")
	private String nome;
	
	@Column(name = "TX_TELEFONE")
	private String telefone;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_NASCIMENTO")
	private Date dataNascimento;
	
	@Column(name = "IN_SEXO")
	private Character sexo;
	
    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA")
    private Empresa empresa;
	
	@OneToMany(mappedBy = "cliente")
    private List<Permissao> permissoes;
	
	@Transient
	private Permissao ultimaPermissao;
	
	@Transient
	private boolean flFazerPedidos;
	
	@Transient
	private Empresa empresaLogada;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Permissao getUltimaPermissao() {
		return ultimaPermissao;
	}

	public void setUltimaPermissao(Permissao ultimaPermissao) {
		this.ultimaPermissao = ultimaPermissao;
	}

	public boolean isFlFazerPedidos() {
		return flFazerPedidos;
	}

	public void setFlFazerPedidos(boolean flFazerPedidos) {
		this.flFazerPedidos = flFazerPedidos;
	}

	public Empresa getEmpresaLogada() {
		return empresaLogada;
	}

	public void setEmpresaLogada(Empresa empresaLogada) {
		this.empresaLogada = empresaLogada;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Date getDataExpiracaoLogin() {
		return dataExpiracaoLogin;
	}

	public void setDataExpiracaoLogin(Date dataExpiracaoLogin) {
		this.dataExpiracaoLogin = dataExpiracaoLogin;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
