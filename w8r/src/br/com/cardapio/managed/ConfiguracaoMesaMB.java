package br.com.cardapio.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.model.map.MapModel;

import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;

@ManagedBean
@ViewScoped
public class ConfiguracaoMesaMB extends BaseManagedBean<Mesa> {
    private static final long serialVersionUID = 1L;
    
    private MapModel mapModel;

    public ConfiguracaoMesaMB() {
        this.setClazz(Mesa.class);
//        carregarMapa();
    }
    
    
//    <p:separator />                 
//    <p:gmap center="#{configuracaoMesaMB.mesaLogada.empresa.latitude}, #{configuracaoMesaMB.mesaLogada.empresa.longitude}" 
//        zoom="15" type="HYBRID" style="width:100%;height:15em" widgetVar="gmap"
//        model="#{configuracaoMesaMB.mapModel}" />

//    public void carregarMapa(){
//    	Empresa empresa = getEmpresaReferencia();
//    	mapModel = new DefaultMapModel();    
//        LatLng coordEmpresa = new LatLng(empresa.getLatitude(), empresa.getLongitude()); 
//        mapModel.addOverlay(new Marker(coordEmpresa, empresa.getNome())); 
//    }
    
    public void selecionaLinguaMesa(ValueChangeEvent event){
        Lingua lingua = (Lingua)event.getNewValue();
        setLinguaLogada(lingua);
    }
    
//    public void acessaFacebook(ActionEvent event){
//    	FacebookXmlRestClient face = (FacebookXmlRestClient)JSFHelper.getSession().getAttribute("facebook.user.client");
//    	try {
//			face.friends_getLists();
//		} catch (FacebookException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//    
//    public void acessaFacebook(){
//    	FacebookXmlRestClient face = (FacebookXmlRestClient)JSFHelper.getSession().getAttribute("facebook.user.client");
//    	try {
//			face.friends_getLists();
//		} catch (FacebookException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}
    
}
