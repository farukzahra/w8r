package br.com.cardapio.managed;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.CryptMD5;

@ManagedBean
@ViewScoped
public class EmpresaMB extends BaseManagedBean<Empresa> {

	private static final long serialVersionUID = 1L;
	private String senhaAtualCrypt = "";
	private MapModel emptyModel;

	public EmpresaMB() {
		this.setClazz(Empresa.class);
		emptyModel = new DefaultMapModel();
		try {
			setEntityList(new EmpresaBO().listAll());
		} catch (BancoDadosException e) {
			addError(e.getMessage());
		}
	}

	
	public void actionCarregar(ActionEvent event) {
		if(getEntity() != null && getEntity().getId() > 0){
			new EmpresaBO().cadastrarDadosEmpresa(getEntity());
		}
	}
	
	@Override
	public void actionPersist(ActionEvent event) {
		if (getEntity().getPerfil() == null)
			getEntity().setPerfil(Empresa.PERFIL_NORMAL);

		if (getEntity().getSenha() != null && !getEntity().getSenha().isEmpty()
				&& !getEntity().getSenha().equals(senhaAtualCrypt))
			getEntity().setSenha(CryptMD5.encrypt(getEntity().getSenha()));
		else
			getEntity().setSenha(senhaAtualCrypt);

//		List<Marker> markers = emptyModel.getMarkers();
//		for (Marker marker : markers) {
//			getEntity().setLatitude(marker.getLatlng().getLat());
//			getEntity().setLongitude(marker.getLatlng().getLng());
//		}
		super.actionPersist(event);
		//setEntity(new Empresa());
		RequestContext.getCurrentInstance().update("cmForm:pnMain");
	}

	public void onMarkerDrag(MarkerDragEvent event) {
		Marker marker = event.getMarker();
		Empresa empresa = getEntity();
		if (empresa != null) {
			empresa.setLatitude(marker.getLatlng().getLat());
			empresa.setLongitude(marker.getLatlng().getLng());
		}
	}

	public void getCordinates() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> parameterMap = context.getExternalContext()
				.getRequestParameterMap();
		double latitude = Double.parseDouble((String) parameterMap
				.get("latitude"));
		double longitude = Double.parseDouble((String) parameterMap
				.get("longitude"));
		System.out.println(latitude + " " + longitude);
	}

	@Override
	public void setEntity(Empresa entity) {
		super.setEntity(entity);
		if (entity != null)
			senhaAtualCrypt = entity.getSenha();
		else
			senhaAtualCrypt = "";

	}

	public Double getLatitude() {
		if (getEntity() != null && getEntity().getLatitude() != null)
			return getEntity().getLatitude();
		else
			return -25.435814;
	}

	public Double getLongitude() {
		if (getEntity() != null && getEntity().getLongitude() != null)
			return getEntity().getLongitude();
		else
			return -49.273492;
	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

}
