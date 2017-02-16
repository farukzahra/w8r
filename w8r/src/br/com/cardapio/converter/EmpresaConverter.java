package br.com.cardapio.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.entity.Empresa;

@FacesConverter(forClass = Empresa.class, value="empresa")
public class EmpresaConverter implements Converter, Serializable {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        
        try {
            if (value != null && !value.trim().isEmpty()) {
                final Integer id = Integer.parseInt(value);
                return new EmpresaBO().find(id);
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
        		Empresa empresa = (Empresa) value;
        		return String.valueOf(empresa.getId());
        	}
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return "";
    }
}