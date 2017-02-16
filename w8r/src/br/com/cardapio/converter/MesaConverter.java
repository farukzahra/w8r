package br.com.cardapio.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pergunta;

@FacesConverter(forClass = Mesa.class, value="mesa")
public class MesaConverter implements Converter, Serializable {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        
        try {
            if (value != null && !value.trim().isEmpty()) {
                final Integer id = Integer.parseInt(value);
                return new MesaBO().find(id);
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
        		Mesa mesa = (Mesa) value;
        		return String.valueOf(mesa.getId());
        	}
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return "";
    }
}