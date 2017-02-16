package br.com.cardapio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the EMPRESA database table.
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final char STATUS_ATIVO = 'A';

    public static final char STATUS_INATIVO = 'I';

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA")
    private Empresa empresa;

    @Column(name = "DS_LOGIN")
    private String login = "";

    @Column(name = "IN_STATUS")
    private Character status = STATUS_ATIVO;

    @Column(name = "TX_SENHA", nullable = false)
    private String senha = "";

    @Column(name = "IN_PERFIL")
    private Character perfil = Empresa.PERFIL_COZINHA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Character getPerfil() {
        return perfil;
    }

    public void setPerfil(Character perfil) {
        this.perfil = perfil;
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
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
