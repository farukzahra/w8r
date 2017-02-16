package br.com.cardapio.managed;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.GrupoProdutoTraducao;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.util.Mensagens;
import br.com.cardapio.util.Tradutor;

@ManagedBean
@SessionScoped
public class GrupoProdutoMB extends BaseManagedBean<GrupoProduto> {
    private static final long serialVersionUID = 1L;

    private List<GrupoProduto> gruposPais;

    private UploadedFile arquivoImagem;

    private StreamedContent imagem;

    private static final String PATH = Mensagens.getMensagem(Mensagens.PATH_IMAGEM_USUARIO);

    private List<GrupoProdutoTraducao> traducoes;

    private Integer progress = 0;

    private List<Integer> ordenacoes;

    public GrupoProdutoMB() {
        this.setClazz(GrupoProduto.class);
        try {
            gruposPais = new GrupoProdutoBO().listarPorStatus(true);
            setEntityList(new GrupoProdutoBO().listarPorEmpresa(getEmpresaLogada().getId()));
            carregarTraducoes(getEntity());
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA), e.getMessage());
        } catch (IntegridadeReferencialException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA), e.getMensagem());
        }
    }

    public Integer getProgress() {
        if (progress > 100) {
            progress = 0;
            return 100;
        } else {
            return progress;
        }
    }

    public void traduzCampos() {
        String nome = getEntity().getNome();
        Lingua linguaPadrao = getEmpresaLogada().getLinguaPadrao();
        int somatorio = 100 / traducoes.size();
        progress = 0;
        for (GrupoProdutoTraducao traducao : traducoes) {
            progress += somatorio + 1;
            try {
                traducao.setNome(Tradutor.traduzirAuto(nome, linguaPadrao, traducao.getLingua()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<GrupoProduto> getEntityList() {
        try {
            return new GrupoProdutoBO().listarPorEmpresa(getEmpresaLogada().getId());
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS));
            e.printStackTrace();
        }
        return null;
    }

    public UploadedFile getArquivoImagem() {
        return arquivoImagem;
    }

    public void setArquivoImagem(UploadedFile arquivoImagem) {
        this.arquivoImagem = arquivoImagem;
    }

    public void carregarArquivo(FileUploadEvent event) {
        try {
            arquivoImagem = event.getFile();
            imagem = new DefaultStreamedContent(arquivoImagem.getInputstream());
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
    }

    private void limpaArquivosAntigos(final GrupoProduto grupoProduto) {
        File diretorio = new File(PATH + "/imgGrupoProduto/");
        FileFilter filtro = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().startsWith("Empr_" + getEmpresaLogada().getId() + "_GrpPrd_" + grupoProduto.getId() + "_");
            }
        };
        File[] arquivos = diretorio.listFiles(filtro);
        for (File file : arquivos) {
            System.out.println("Deletando " + file.getName());
            file.delete();
        }
    }

    @Override
    public void actionPersist(ActionEvent event) {
        try {
            GrupoProduto grupoProduto = getEntity();
            setTraducoes(grupoProduto);
            if (arquivoImagem != null) {
                limpaArquivosAntigos(grupoProduto);
                byte[] vet = arquivoImagem.getContents();
                String extensao = arquivoImagem.getFileName().split("\\.")[1];
                String nomeArquivo = "/imgGrupoProduto/Empr_" + getEmpresaLogada().getId() + "_GrpPrd_" + grupoProduto.getId() + "." + extensao.toLowerCase();
                String path = PATH + nomeArquivo;
                File file = new File(path);
                FileImageOutputStream out = new FileImageOutputStream(file);
                out.write(vet);
                out.close();
                grupoProduto.setPathImagem(nomeArquivo);
            }
            grupoProduto.setEmpresa(getEmpresaLogada());
            imagem = null;
            arquivoImagem = null;
            super.actionPersist(event);
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_SALVAR_REGISTRO), "");
        }
    }

    @Override
    public void setEntity(GrupoProduto entity) {
        try {
            if (entity != null && entity.getPathImagem() != null) {
                imagem = new DefaultStreamedContent(new FileInputStream(new File(PATH + entity.getPathImagem())));
            }else{
                imagem = null;
                arquivoImagem = null;
            }
            carregarTraducoes(entity);
        } catch (Exception e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA), e.getMessage());
        }
        super.setEntity(entity);
    }

    private void setTraducoes(GrupoProduto produto) {
        List<GrupoProdutoTraducao> pts = new ArrayList<GrupoProdutoTraducao>();
        for (GrupoProdutoTraducao pt : traducoes) {
            if (pt.getNome() != null && !pt.getNome().isEmpty()) {
                pts.add(pt);
            }
        }
        produto.setTraducoes(pts);
    }

    private void carregarTraducoes(GrupoProduto grupoProduto) throws BancoDadosException, IntegridadeReferencialException {
        traducoes = new ArrayList<GrupoProdutoTraducao>();
        List<Lingua> linguas = getLinguasDisponiveisSemPrincipal();
        // List<GrupoProdutoTraducao> traducoesGrupo = new GrupoProdutoTraducaoBO().listarPorGrupo(grupoProduto.getId());
        for (Lingua lingua : linguas) {
            GrupoProdutoTraducao pt = new GrupoProdutoTraducao();
            pt.setGrupoProduto(grupoProduto);
            pt.setLingua(lingua);
            if (grupoProduto != null && grupoProduto.getTraducoes() != null && !grupoProduto.getTraducoes().isEmpty())
                for (GrupoProdutoTraducao trad : grupoProduto.getTraducoes()) {
                    if (lingua.equals(trad.getLingua())) {
                        pt = trad;
                    }
                }
            traducoes.add(pt);
        }
    }

    public StreamedContent getImagem() {
        return imagem;
    }

    public void setImagem(StreamedContent imagem) {
        this.imagem = imagem;
    }

    public List<GrupoProduto> getGruposPais() {
        return gruposPais;
    }

    public void setGruposPais(List<GrupoProduto> gruposPais) {
        this.gruposPais = gruposPais;
    }

    public List<GrupoProdutoTraducao> getTraducoes() {
        return traducoes;
    }

    public void setTraducoes(List<GrupoProdutoTraducao> traducoes) {
        this.traducoes = traducoes;
    }

    public List<Integer> getOrdenacoes() {
        try {
            setEntityList(new GrupoProdutoBO().listarPorEmpresa(getEmpresaLogada().getId()));
            if (getEntityList() != null && !getEntityList().isEmpty()) {
                ordenacoes = new ArrayList<Integer>();
                for (int i = 1; i <= getEntityList().size(); i++) {
                    ordenacoes.add(i);
                }
            }
        } catch (BancoDadosException e) {
            e.printStackTrace();
        }
        return ordenacoes;
    }

    public void setOrdenacoes(List<Integer> ordenacoes) {
        this.ordenacoes = ordenacoes;
    }
}
