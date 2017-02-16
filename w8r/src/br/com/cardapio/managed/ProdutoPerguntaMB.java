package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.bo.PerguntaBO;
import br.com.cardapio.bo.ProdutoBO;
import br.com.cardapio.bo.ProdutoPerguntaBO;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.ProdutoPergunta;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class ProdutoPerguntaMB extends BaseManagedBean<ProdutoPergunta> {
    private static final long serialVersionUID = 1L;

    private List<GrupoProduto> gruposDisponiveis = new ArrayList<GrupoProduto>();

    private GrupoProduto grupoSelecionado;

    private List<Produto> produtosDisponiveis = new ArrayList<Produto>();

    private List<Pergunta> perguntasDisponiveis = new ArrayList<Pergunta>();

    private boolean flRespostaObrigatoria;

    public ProdutoPerguntaMB() {
        this.setClazz(ProdutoPergunta.class);
        setBo(new ProdutoPerguntaBO());
        try {
            perguntasDisponiveis = new PerguntaBO().listarPorEmpresa(getEmpresaLogada().getId());
            gruposDisponiveis = new GrupoProdutoBO().listarPorEmpresaEAtivo(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
        }
    }
    
    public void carregarProdutoPergunta() {
        try {
            ProdutoPerguntaBO bo = new ProdutoPerguntaBO();
            ProdutoPergunta produtoPergunta = bo.buscarPorProdutoEPergunta(getEntity().getProduto().getId(), getEntity().getPergunta().getId());
            if(produtoPergunta != null){
                super.setEntity(produtoPergunta);
            }
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }
    
    public void carregarEntitiesList() {
        try {
            ProdutoPerguntaBO bo = new ProdutoPerguntaBO();
            List<ProdutoPergunta> produtosPerguntas = bo.listarPorProduto(getEntity().getProduto().getId());
            setEntityList(produtosPerguntas);
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
    }

    @Override
    public void actionPersist(ActionEvent event) {
        try {
            ProdutoPergunta produtoPergunta = getEntity();
            Produto produto = getEntity().getProduto();
            if (!produto.getProdutoPerguntas().contains(produtoPergunta)) {
                produto.getProdutoPerguntas().add(produtoPergunta);
            } else {
                produto.getProdutoPerguntas().set(produto.getProdutoPerguntas().indexOf(produtoPergunta), produtoPergunta);
            }
            if (new ProdutoBO().persist(produto)) {
                actionNew(event);
            }
            addInfo(Mensagens.getMensagem(Mensagens.REGISTRO_SALVO_COM_SUCESSO), "");
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO));
        } catch (IntegridadeReferencialException e) {
        	addError(e.getMensagem());
		} catch (RegistroExistenteException e) {
			addError(e.getMensagem());
		}
    }

    @Override
    public void actionRemove(ActionEvent event) {
        if(getEntity() != null) {
            //Produto produto = getEntity().getProduto();
            //produto.getProdutoPerguntas().remove(getEntity());
            super.actionRemove(event);
        }
    }

    public List<Pergunta> getPerguntasDisponiveis() {
        return perguntasDisponiveis;
    }

    public void setPerguntasDisponiveis(List<Pergunta> perguntasDisponiveis) {
        this.perguntasDisponiveis = perguntasDisponiveis;
    }

    @Override
    public List<ProdutoPergunta> getEntityList() {
        if (getEntity().getProduto() != null) {
            try {
                return ((ProdutoPerguntaBO)getBo()).listarPorProduto(getEntity().getProduto().getId());
            } catch (BancoDadosException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public boolean isFlRespostaObrigatoria() {
        return flRespostaObrigatoria;
    }

    public void setFlRespostaObrigatoria(boolean flRespostaObrigatoria) {
        this.flRespostaObrigatoria = flRespostaObrigatoria;
    }

    public List<Produto> getProdutosDisponiveis() {
        try {
            if (grupoSelecionado != null)
                produtosDisponiveis =  new ProdutoBO().buscarProdutosAtivosPorGrupo(grupoSelecionado.getId());
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
        }
        return produtosDisponiveis;
    }

    public void setProdutosDisponiveis(List<Produto> produtosDisponiveis) {
        this.produtosDisponiveis = produtosDisponiveis;
    }

    public List<GrupoProduto> getGruposDisponiveis() {
        return gruposDisponiveis;
    }

    public void setGruposDisponiveis(List<GrupoProduto> gruposDisponiveis) {
        this.gruposDisponiveis = gruposDisponiveis;
    }

    public GrupoProduto getGrupoSelecionado() {
        return grupoSelecionado;
    }

    public void setGrupoSelecionado(GrupoProduto grupoSelecionado) {
        this.grupoSelecionado = grupoSelecionado;
    }
}