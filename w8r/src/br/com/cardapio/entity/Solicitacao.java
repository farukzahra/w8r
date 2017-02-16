package br.com.cardapio.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

@Entity
@Table(name = "SOLICITACAO")
public class Solicitacao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SOLICITACAO", unique = true, nullable = false)
    private Integer id;

    @Column(name = "DS_SOLICITACAO")
    private String descricao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CADASTRO")
    private Date dataCadastro = Calendar.getInstance().getTime();

    @ManyToOne
    @JoinColumn(name = "ID_MESA")
    private Mesa mesa;

    @Column(name = "TP_SOLICITACAO")
    private String tipo = FECHAR_CONTA;

    @Column(name = "ST_SOLICITACAO")
    private String status;

    public static final String ATENDIDA = "A";

    public static final String NAO_ATENDIDA = "N";

    public static final String FECHAR_CONTA = "Fechar Conta";

    public static final String DUVIDA = "Dúvida";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
    
    public String getDataCadastroStr() {
        if(dataCadastro != null){
            return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataCadastro);
        }
        return "";
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        Solicitacao other = (Solicitacao) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
