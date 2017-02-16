package br.com.cardapio.util;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JSFHelper {

	public static HttpSession getSession() {
		HttpSession httpSession = (HttpSession) getExternalContext().getSession(true);
		return httpSession;
	}
	
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest)getExternalContext().getRequest();
		return request;
	}
	
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = (HttpServletResponse)getExternalContext().getResponse();
		return response;
	}
	
	public static Application getApplication() {
		return FacesContext.getCurrentInstance().getApplication();
	}
	
	public static ExternalContext getExternalContext(){
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static void redirect(String path){
		try {
			getExternalContext().redirect(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static UIViewRoot getUIViewRoot(){
		return FacesContext.getCurrentInstance().getViewRoot();
	}
	
	public static Object getActionAttribute(ActionEvent event, String name) {
        return event.getComponent().getAttributes().get(name);
    }
	
}