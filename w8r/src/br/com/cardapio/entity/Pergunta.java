package br.com.cardapio.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the PERGUNTA database table.
 */
@Entity
@Table(name = "PERGUNTA")
public class Pergunta implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TIPO_RESPOSTA_UMA_EM_VARIAS = "UMA_EM_VARIAS";
    public static final String TIPO_RESPOSTA_VARIAS_EM_VARIAS = "VARIAS_EM_VARIAS";
    public static final String TIPO_RESPOSTA_SIM_OU_NAO = "SIM_OU_NAO";
    public static final String TIPO_RESPOSTA_QUANTIDADE = "QUANTIDADE";
    public static final String TIPO_RESPOSTA_TEXTO = "TEXTO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERGUNTA", unique = true, nullable = false)
    private Integer id;

    @Column(name = "DS_PERGUNTA")
    private String descricao;

    @Column(name = "IN_TIPO_RESPOSTA")
    private String tipoResposta;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA")
    private Empresa empresa;

    @OneToMany(mappedBy = "pergunta")
    private List<ProdutoPergunta> produtoPerguntas;

    @OneToMany(mappedBy = "pergunta")
    private List<Resposta> respostas;
    
    @OneToMany(mappedBy = "pergunta", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<PerguntaTraducao> traducoes;
    
    @Transient
	private Lingua linguaTraduzida;
    
    

    public Pergunta(String descricao, String tipoResposta, Empresa empresa) {
		super();
		this.descricao = descricao;
		this.tipoResposta = tipoResposta;
		this.empresa = empresa;
	}

	public Pergunta() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer idPergunta) {
        this.id = idPergunta;
    }

    public String getDescricao() {
		if(linguaTraduzida == null || linguaTraduzida == empresa.getLinguaPadrao())
			return this.descricao;
		else{
			for(PerguntaTraducao traducao : traducoes)
				if(traducao.getLingua() == linguaTraduzida && traducao.getDescricao() != null)
					return traducao.getDescricao();
			for(PerguntaTraducao traducao : traducoes)
				if(traducao.getLingua() == Lingua.EN && traducao.getDescricao() != null)
					return traducao.getDescricao();
		}		
		return this.descricao;
	}

    public void setDescricao(String DescPergunta) {
        this.descricao = DescPergunta;
    }

    public String getTipoResposta() {
        return this.tipoResposta;
    }

    public void setTipoResposta(String tipoResposta) {
        this.tipoResposta = tipoResposta;
    }

    public List<ProdutoPergunta> getProdutoPerguntas() {
        return this.produtoPerguntas;
    }

    public void setProdutoPerguntas(List<ProdutoPergunta> produtoPerguntas) {
        this.produtoPerguntas = produtoPerguntas;
    }

    public List<Resposta> getRespostas() {
        return this.respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getTipoRespostaUmaEmVarias() {
        return TIPO_RESPOSTA_UMA_EM_VARIAS;
    }

    public String getTipoRespostaVariasEmVarias() {
        return TIPO_RESPOSTA_VARIAS_EM_VARIAS;
    }

    public String getTipoRespostaSimOuNao() {
        return TIPO_RESPOSTA_SIM_OU_NAO;
    }

    public String getTipoRespostaQuantidade() {
        return TIPO_RESPOSTA_QUANTIDADE;
    }

    public String getTipoRespostaTexto() {
        return TIPO_RESPOSTA_TEXTO;
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
        Pergunta other = (Pergunta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public List<PerguntaTraducao> getTraducoes() {
        return traducoes;
    }

    public void setTraducoes(List<PerguntaTraducao> traducoes) {
        this.traducoes = traducoes;
    }

	public Lingua getLinguaTraduzida() {
		return linguaTraduzida;
	}

	public void setLinguaTraduzida(Lingua linguaTraduzida) {
		this.linguaTraduzida = linguaTraduzida;
	}
	
	public boolean isTraduzida(){
		return (linguaTraduzida != null && !linguaTraduzida.equals(empresa.getLinguaPadrao()))? false : true;
	}
}
