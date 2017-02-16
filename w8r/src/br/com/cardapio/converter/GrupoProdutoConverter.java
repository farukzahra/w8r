package br.com.cardapio.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.entity.GrupoProduto;

@FacesConverter(forClass = GrupoProduto.class, value="grupoProduto")
public class GrupoProdutoConverter implements Converter, Serializable {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        
        try {
            if (value != null && !value.trim().isEmpty()) {
                final Integer id = Integer.parseInt(value);
                return new GrupoProdutoBO().find(GrupoProduto.class, id);
            }
        } catch (Exception e) {
            e.printStackTrace();        
        }    
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        try {
        	if(value != null){
        	    GrupoProduto grupoProduto = (GrupoProduto) value;
        		return String.valueOf(grupoProduto.getId());
        	}
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return "";
    }
}