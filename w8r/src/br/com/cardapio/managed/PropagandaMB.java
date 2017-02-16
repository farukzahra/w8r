package br.com.cardapio.managed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.cardapio.bo.PropagandaBO;
import br.com.cardapio.entity.Propaganda;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@SessionScoped
public class PropagandaMB extends BaseManagedBean<Propaganda> {
    private UploadedFile arquivoImagemDetalhada;

    private StreamedContent streamImagemDetalhada;

    private static final String PATH = Mensagens.getMensagem(Mensagens.PATH_IMAGEM_USUARIO);

    public PropagandaMB() {
        this.setClazz(Propaganda.class);
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
    public void actionRemove(ActionEvent event) {
        String path = PATH + getEntity().getPathImagem();
        File file = new File(path);
        file.delete();
        streamImagemDetalhada = null;
        super.actionRemove(event);
    }
    
    @Override
    public List<Propaganda> getEntityList() {
        try {
            return new PropagandaBO().listByEmpresa(getEmpresaReferencia().getId());
        } catch (BancoDadosException e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
        return null;
    }

    @Override
    public void actionPersist(ActionEvent event) {
        try {
            Propaganda propaganda = getEntity();
            streamImagemDetalhada = null;
            propaganda.setEmpresa(getEmpresaLogada());
            super.actionPersist(event);
            String pathImagemDetalhada = getPathImagem(arquivoImagemDetalhada, propaganda);
            if (pathImagemDetalhada != null)
                propaganda.setPathImagem(pathImagemDetalhada);
            PropagandaBO bo = new PropagandaBO();
            bo.persist(propaganda);
        } catch (Exception e) {
            e.printStackTrace();
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_BUSCAR_REGISTROS), "");
        }
    }

    private String getPathImagem(UploadedFile imagem, Propaganda propaganda) throws Exception {
        String nomeArquivo = null;
        if (imagem != null) {
            // limpaArquivosAntigos(produto);
            byte[] vet = imagem.getContents();
            String extensao = imagem.getFileName().split("\\.")[1];
            nomeArquivo = "/imgPropaganda/Empr_" + getEmpresaLogada().getId() + "_Prd_" + propaganda.getId() + "." + extensao.toLowerCase();
            String path = PATH + nomeArquivo;
            File file = new File(path);
            FileImageOutputStream out = new FileImageOutputStream(file);
            out.write(vet);
            out.close();
        }
        return nomeArquivo;
    }
    
    @Override
    public void setEntity(Propaganda entity) {
        try {
            if(entity != null){
                if (entity.getPathImagem() != null) {
                    streamImagemDetalhada = new DefaultStreamedContent(new FileInputStream(new File(PATH + entity.getPathImagem())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setEntity(entity);
    }

    public StreamedContent getStreamImagemDetalhada() {
        return streamImagemDetalhada;
    }

    public void setStreamImagemDetalhada(StreamedContent streamImagemDetalhada) {
        this.streamImagemDetalhada = streamImagemDetalhada;
    }
}
