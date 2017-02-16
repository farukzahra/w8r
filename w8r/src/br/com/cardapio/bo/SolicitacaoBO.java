package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.Solicitacao;
import br.com.cardapio.exception.BancoDadosException;

public class SolicitacaoBO extends BOBase<Solicitacao> {
    private static final long serialVersionUID = 1L;

    public SolicitacaoBO() {
        super();
        setClazz(Solicitacao.class);
    }

    public List<Solicitacao> listarPorEmpresaNaoAtendidas(Integer idEmpresa) throws BancoDadosException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mesa.empresa.id", idEmpresa);
        parametros.put("status", Solicitacao.NAO_ATENDIDA);
        String[] ordenacao = new String[] { "dataCadastro" };
        return listByFields(parametros, ordenacao);
    }
}
