package br.com.cardapio.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.cardapio.entity.StatusPedido;

@FacesConverter(forClass = StatusPedido.class, value="statusPedido")
public class StatusPedidoConverter implements Converter, Serializable {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        
        try {
            if (value != null && !value.trim().isEmpty()) {
                return StatusPedido.valueOf(value);
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
        		StatusPedido status = (StatusPedido) value;
        		return status.name();
        	}
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return "";
    }
}