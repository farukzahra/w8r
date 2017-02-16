package br.com.cardapio.managed;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.bo.ProdutoBO;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.ProdutoTraducao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;
import br.com.cardapio.util.Tradutor;

@ManagedBean
@SessionScoped
public class ProdutoMB extends BaseManagedBean<Produto> {
    private static final long serialVersionUID = 1L;

    private List<GrupoProduto> grupos;

    private UploadedFile arquivoImagemDetalhada;

    private UploadedFile arquivoImagemPrincipal;

    private StreamedContent streamImagemDetalhada;

    private StreamedContent streamImagemPrincipal;

    private static final String PATH = Mensagens.getMensagem(Mensagens.PATH_IMAGEM_USUARIO);

    private GrupoProduto grupoProdutoSelecionado;

    private List<ProdutoTraducao> traducoes;

    private Integer progress = 0;

    private List<Integer> ordenacoes;

    public Integer getProgress() {
        if (progress > 100) {
            progress = 0;
            return 100;
        } else {
            return progress;
        }
    }

    public ProdutoMB() {
        this.setClazz(Produto.class);
        try {
            if (getEmpresaLogada() == null)
                JSFHelper.redirect("loginempresa.jsf?faces-redirect=true");
            grupos = new GrupoProdutoBO().listarPorEmpresaEAtivo(getEmpresaLogada().getId());
            List<Produto> produtos = new ProdutoBO().buscarProdutoPorEmpresa(getEmpresaLogada().getId());
            setEntityList(produtos);
            carregarTraducoes(getEntity());
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(e.getMessage());
        }
    }

    public GrupoProduto getGrupoProdutoSelecionado() {
        return grupoProdutoSelecionado;
    }

    public void traduzCampos() {
        String nome = getEntity().getNome();
        String descricao = getEntity().getDescricao();
        Lingua linguaPadrao = getEmpresaLogada().getLinguaPadrao();
        int somatorio = 100 / traducoes.size();
        progress = 0;
        for (ProdutoTraducao traducao : traducoes) {
            progress += somatorio + 1;
            try {
                traducao.setNome(Tradutor.traduzirAuto(nome, linguaPadrao, traducao.getLingua()));
                traducao.setDescricao(Tradutor.traduzirAuto(descricao, linguaPadrao, traducao.getLingua()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setGrupoProdutoSelecionado(GrupoProduto grupoProdutoSelecionado) {
        this.grupoProdutoSelecionado = grupoProdutoSelecionado;
        getEntity().setGrupoProduto(grupoProdutoSelecionado);       
        carregaOrdenacoes(grupoProdutoSelecionado);
    }

    public void carregarArquivoImgPrincipal(FileUploadEvent event) {
        try {
            arquivoImagemPrincipal = event.getFile();
            streamImagemPrincipal = new DefaultStreamedContent(arquivoImagemPrincipal.getInputstream());
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
    }

    public void carregarArquivoImgDetalhada(FileUploadEvent event) {
        try {
            arquivoImagemDetalhada = event.getFile();
            streamImagemDetalhada = new DefaultStreamedContent(arquivoImagemDetalhada.getInputstream());
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
    }

    @Override
    public void actionPersist(ActionEvent event) {
        try {
            Produto produto = getEntity();
            setTraducoes(produto);
            if (produto.getFlDestaque() && streamImagemDetalhada == null) {
                addError(Mensagens.getMensagem(Mensagens.PRODUTO_IMAGEM_OBRIGATORIA), "");
            } else {
                super.actionPersist(event);
                if(arquivoImagemDetalhada != null) {
                    String pathImagemDetalhada = getPathImagem(arquivoImagemDetalhada, produto);
                    if (pathImagemDetalhada != null)
                        produto.setPathImagemDetalhada(pathImagemDetalhada);
                }
                if(arquivoImagemDetalhada != null) {
                    String pathImagemPrincipal = diminueImagem(arquivoImagemDetalhada, produto);
                    if (pathImagemPrincipal != null)
                        produto.setPathImagemPrincipal(pathImagemPrincipal);
                }
                ProdutoBO bo = new ProdutoBO();
                bo.persist(produto);
                streamImagemDetalhada = null;
                streamImagemPrincipal = null;
                arquivoImagemDetalhada = null;
                arquivoImagemPrincipal = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
    }

    @Override
    public List<Produto> getEntityList() {
        try {
            return new ProdutoBO().buscarProdutoPorEmpresa(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProdutoBO getBo() {
        return (ProdutoBO) super.getBo();
    }

    private void limpaArquivosAntigos(final Produto produto) {
        File diretorio = new File(PATH + "/imgProduto/");
        FileFilter filtro = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().startsWith("Empr_" + getEmpresaLogada().getId() + "_Prd_" + produto.getId() + "_");
            }
        };
        File[] arquivos = diretorio.listFiles(filtro);
        if (arquivos != null && arquivos.length > 0) {
            for (File file : arquivos) {
                System.out.println("Deletando " + file.getName());
                file.delete();
            }
        }
    }

    private String diminueImagem(UploadedFile imagem, Produto produto) throws Exception {
        String nomeArquivo = null;
        if (imagem != null) {
            BufferedImage originalImage = ImageIO.read(imagem.getInputstream());
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = new BufferedImage(80, 65, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 80, 65, null);
            g.dispose();
            String extensao = imagem.getFileName().split("\\.")[1];
            nomeArquivo = "/imgProduto/Empr_" + getEmpresaLogada().getId() + "_Prd_" + produto.getId() + "_peq_." + extensao.toLowerCase();
            String path = PATH + nomeArquivo;
            ImageIO.write(resizedImage, extensao, new File(path));
        }
        return nomeArquivo;
    }

    private String getPathImagem(UploadedFile imagem, Produto produto) throws Exception {
        String nomeArquivo = null;
        if (imagem != null) {
            limpaArquivosAntigos(produto);
            byte[] vet = imagem.getContents();
            String extensao = imagem.getFileName().split("\\.")[1];
            nomeArquivo = "/imgProduto/Empr_" + getEmpresaLogada().getId() + "_Prd_" + produto.getId() + "." + extensao.toLowerCase();
            String path = PATH + nomeArquivo;
            File file = new File(path);
            FileImageOutputStream out = new FileImageOutputStream(file);
            out.write(vet);
            out.close();
        }
        return nomeArquivo;
    }
    
    public void carregaOrdenacoes(GrupoProduto gp){
        try {
            if(gp != null) {
                List<Produto> produtos = new ProdutoBO().buscarProdutoPorGrupo(gp.getId());
                if (produtos != null && !produtos.isEmpty()) {
                    ordenacoes = new ArrayList<Integer>();
                    for (int i = 1; i <= produtos.size(); i++) {
                        ordenacoes.add(i);
                    }
                }
                grupoProdutoSelecionado = gp;
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    @Override
    public void setEntity(Produto entity) {
        try {
            if (entity != null && entity.getGrupoProduto() != null) {
                carregaOrdenacoes(entity.getGrupoProduto());
                carregarTraducoes(entity);
                if (entity.getPathImagemDetalhada() != null) {
                    streamImagemDetalhada = new DefaultStreamedContent(new FileInputStream(new File(PATH + entity.getPathImagemDetalhada())));
                }else{
                    streamImagemDetalhada = null;
                    arquivoImagemDetalhada = null;
                }
                if (entity.getPathImagemPrincipal() != null) {
                    streamImagemPrincipal = new DefaultStreamedContent(new FileInputStream(new File(PATH + entity.getPathImagemPrincipal())));
                }else{
                    streamImagemPrincipal = null;
                    arquivoImagemPrincipal = null;
                }               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setEntity(entity);
    }

    private void setTraducoes(Produto produto) {
        List<ProdutoTraducao> pts = new ArrayList<ProdutoTraducao>();
        for (ProdutoTraducao pt : traducoes) {
            if (pt.getNome() != null && !pt.getNome().isEmpty()) {
                pts.add(pt);
            }
        }
        produto.setTraducoes(pts);
    }

    private void carregarTraducoes(Produto produto) {
        traducoes = new ArrayList<ProdutoTraducao>();
        List<Lingua> linguas = getLinguasDisponiveisSemPrincipal();
        for (Lingua lingua : linguas) {
            ProdutoTraducao pt = new ProdutoTraducao();
            pt.setProduto(produto);
            pt.setLingua(lingua);
            if (produto != null && produto.getTraducoes() != null)
                for (ProdutoTraducao trad : produto.getTraducoes()) {
                    if (lingua.equals(trad.getLingua())) {
                        pt = trad;
                    }
                }
            traducoes.add(pt);
        }
    }

    public List<GrupoProduto> getGrupos() {
        // como ta session scope tem q deixar isso aqui senao qdo cadastro um grupo e vem pra esta tela nao muda a lista
        try {
            grupos = new GrupoProdutoBO().listarPorEmpresaEAtivo(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return grupos;
    }

    public void setGrupos(List<GrupoProduto> grupos) {
        this.grupos = grupos;
    }

    public UploadedFile getArquivoImagemDetalhada() {
        return arquivoImagemDetalhada;
    }

    public void setArquivoImagemDetalhada(UploadedFile arquivoImagemDetalhada) {
        this.arquivoImagemDetalhada = arquivoImagemDetalhada;
    }

    public UploadedFile getArquivoImagemPrincipal() {
        return arquivoImagemPrincipal;
    }

    public void setArquivoImagemPrincipal(UploadedFile arquivoImagemPrincipal) {
        this.arquivoImagemPrincipal = arquivoImagemPrincipal;
    }

    public StreamedContent getStreamImagemDetalhada() {
        return streamImagemDetalhada;
    }

    public void setStreamImagemDetalhada(StreamedContent streamImagemDetalhada) {
        this.streamImagemDetalhada = streamImagemDetalhada;
    }

    public StreamedContent getStreamImagemPrincipal() {
        return streamImagemPrincipal;
    }

    public void setStreamImagemPrincipal(StreamedContent streamImagemPrincipal) {
        this.streamImagemPrincipal = streamImagemPrincipal;
    }

    public List<ProdutoTraducao> getTraducoes() {
        return traducoes;
    }

    public void setTraducoes(List<ProdutoTraducao> traducoes) {
        this.traducoes = traducoes;
    }

    public List<Integer> getOrdenacoes() {
        return ordenacoes;
    }

    public void setOrdenacoes(List<Integer> ordenacoes) {
        this.ordenacoes = ordenacoes;
    }
}
