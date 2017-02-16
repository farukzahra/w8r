package br.com.cardapio.bo;

import java.util.HashMap;
import java.util.Map;

import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.MetaLocal;
import br.com.cardapio.exception.BancoDadosException;

public class MetaLocalBO extends BOBase<MetaLocal> {
    private static final long serialVersionUID = 1L;

    public MetaLocalBO() {
        super();
        setClazz(MetaLocal.class);
    }
    
    public MetaLocal findByLingua(Lingua lingua, String metaLocal) throws BancoDadosException {
        Map<String, Object> filtros = new HashMap<String, Object>();
        if(lingua == null)
        	lingua = Lingua.PT;
        filtros.put("lingua", lingua.getLocale().getLanguage()); 
        filtros.put("tipoMetaLocal", metaLocal);
        return super.findByFields(filtros);
    }
}
