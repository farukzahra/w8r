package br.com.cardapio.util;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class Mensagens {
    public static final String PATH_SISTEMA = "path.sistema";

    public static final String PATH_IMAGEM_USUARIO = "path.imagem.usuario";

    public static final String REGISTRO_SALVO_COM_SUCESSO = "cm.salvar.sucesso";

    public static final String REGISTRO_REMOVIDO_COM_SUCESSO = "cm.remover.sucesso";

    public static final String ERRO_AO_SALVAR_REGISTRO = "cm.salvar.erro";

    public static final String ERRO_AO_REMOVER_REGISTRO = "cm.remover.erro";

    public static final String ERRO_AO_BUSCAR_REGISTROS = "cm.buscar.erro";
    
    public static final String ERRO_AO_CARREGAR_PAGINA = "cm.carregar.erro"; 

    public static final String USUARIO_SENHA_INVALIDO = "cm.login.user.senha.invalido";

    public static final String USUARIO_SEM_PERFIL = "cm.login.user.sem.perfil";

    public static final String SENHA_EXPIRADA = "cm.login.senha.expirada";

    public static final String LOGIN_ERRO_GENERICO = "cm.login.erro.generico";

    public static final String SENHA_ATUAL_INVALIDA = "cm.trocar.senha.senha.atual.invalida";

    public static final String SENHA_CONFIRMACAO_INVALIDA = "cm.trocar.senha.senha.confirmacao.invalida";

    public static final String USUARIO_NAOENCONTRADO = "cm.login.user.naoencontrado";

    public static final String USUARIO_BLOQUEADO = "cm.login.user.bloqueado";

    public static final String USUARIO_DUPLICADO = "cm.usuario.user.duplicado";

    public static final String EMAIL_INVALIDO = "cm.usuario.email.invalido";

    public static final String RANGE_DATA_INVALIDO = "cm.range.data.invalido";
    
    public static final String EXCEPTION_BUSINESS = "cm.exception.business";
    
    public static final String EXCEPTION_CREDITOS_INSUFICIENTE = "cm.exception.creditos.insuficiente";
    
    public static final String EXCEPTION_INTEGRIDADE_VIOLADA = "cm.exception.integridade.violada";
    
    public static final String EXCEPTION_REGISTRO_EXISTENTE = "cm.exception.registro.existente";
    
    public static final String EXCEPTION_CAMPO_OBRIGATORIO = "javax.faces.component.UIInput.REQUIRED";
    
    public static final String EXCEPTION_SENHA_INVALIDA = "cm.exception.senha.invalida";
    
    public static final String EXCEPTION_USUARIO_BLOQUEADO = "cm.exception.usuario.bloqueado";
    
    public static final String EXCEPTION_USUARIO_NAO_ENCONTRADO = "cm.exception.usuario.nao.encontrado";
    
    public static final String PESQUISA_REALIZADA = "cm.financeiro.pesquisa.realizada";
    
    public static final String STATUS_TROCADO_SUCESSO = "cm.listapedidos.status.trocado.sucesso";
    
    public static final String MENSAGEM_ENVIADA = "cm.mensagem.enviada";

    public static final String MENSAGEM_NOVA = "cm.mensagem.nova";
    
    public static final String MENSAGEM_ACESSO_MENU_MENSAGEM = "cm.mensagem.acesso.menu.mensagens";
    
    public static final String PEDIDO_REALIZADO_SUCESSO = "cm.pedido.realizado.sucesso";
    
    public static final String MESA_FECHADA_SUCESSO = "cm.principalmesa.mesa.fechada.sucesso";
    
    public static final String MESA_REABERTA_SUCESSO = "cm.principalmesa.mesa.reaberta.sucesso";
    
    public static final String PRODUTO_IMAGEM_OBRIGATORIA = "cm.produto.imagem.obrigatoria";
    
    public static String getMensagem(String key) {
        ResourceBundle resource = JSFHelper.getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");
        return resource.getString(key);
    }
}
