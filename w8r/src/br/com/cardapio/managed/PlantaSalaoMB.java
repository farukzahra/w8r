package br.com.cardapio.managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ResizeEvent;

import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PlantaSalaoBO;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.PlantaSalao;
import br.com.cardapio.entity.Ponto;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;

@ManagedBean
@ViewScoped
public class PlantaSalaoMB extends BaseManagedBean<PlantaSalao> {
    private static final long serialVersionUID = 1L;
    private List<Ponto> pontos;
    private int tamX;
    private int tamY;
    private List<Mesa> mesas;
    private Mesa mesaSel;
    MesaBO mesaBO = new MesaBO();
    List<PlantaSalao> plantas = new ArrayList<PlantaSalao>();
    PlantaSalao plantaSel;

    public PlantaSalaoMB() {
        this.setClazz(PlantaSalao.class);
        try {	        
	        mesas  = mesaBO.listarAtivasPorEmpresa(getEmpresaLogada().getId());
	        plantas = new PlantaSalaoBO().listarPorEmpresa(getEmpresaLogada().getId());
	        if(plantas != null && plantas.size() == 1){
	        	setPlantaSel(plantas.get(0));	        	
	        }
		} catch (BancoDadosException e) {
			e.printStackTrace();
		}
                
    }
    
    public void carregarPlanta(){
    	PlantaSalao pSalao = plantas.get(0);
        setEntity(pSalao);
        tamX = pSalao.getLarguraCm();
        tamY = pSalao.getComprimentoCm();
    	pontos = new ArrayList<Ponto>();
        int i = 0;
        for(int y = tamY - 1; y >= 0 ; y--){
        	for(int x = 0; x < tamX; x++){ 
        		pontos.add(new Ponto(x , y, i++));
        	}
        }
        List<Mesa> mesasPosicionadas = new ArrayList<Mesa>();
        for(Mesa mesa : mesas){
        	if(mesa.getPontoPlanta() != null){
	        	Ponto ponto = pontos.get(mesa.getPontoPlanta());
	        	ponto.setMesa(mesa);
	        	mesasPosicionadas.add(mesa);
        	}
		}
        mesas.removeAll(mesasPosicionadas);
    }
    
    public void onDrop(DragDropEvent event) {         
        try {
        	Mesa mesa = (Mesa) event.getData();  
            String strPonto = event.getDropId(); 
            Integer idPonto = Integer.parseInt(strPonto.split(":")[2]);
            mesa.setPontoPlanta(idPonto); 
            mesaBO.merge(mesa);
		} catch (BancoDadosException e) {
			e.printStackTrace();
		} catch (IntegridadeReferencialException e) {
			e.printStackTrace();
		}
    }
    
    public void onResize(ResizeEvent event) {     	
    	try {
    		String idMesa = (String)event.getComponent().getAttributes().get("widgetVar");
			Mesa mesa = mesaBO.find(Integer.parseInt(idMesa));
			mesa.setLargura(event.getWidth());
			mesa.setComprimento(event.getHeight());
			mesaBO.merge(mesa);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
    
    public void delete(ActionEvent event) {  
    	try {
    		String idMesa = (String)event.getComponent().getAttributes().get("fragment");
			Mesa mesa = mesaBO.find(Integer.parseInt(idMesa));
			mesa.setPontoPlanta(null);
			mesa.setPlantaSalao(null);
			mesaBO.merge(mesa);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}

	public int getTamX() {
		return tamX;
	}

	public void setTamX(int tamX) {
		this.tamX = tamX;
	}

	public int getTamY() {
		return tamY;
	}

	public void setTamY(int tamY) {
		this.tamY = tamY;
	}

	public List<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public List<PlantaSalao> getPlantas() {
		return plantas;
	}

	public void setPlantas(List<PlantaSalao> plantas) {
		this.plantas = plantas;
	}

	public PlantaSalao getPlantaSel() {
		return plantaSel;
	}

	public void setPlantaSel(PlantaSalao plantaSel) {
		this.plantaSel = plantaSel;
		carregarPlanta();
	}

	public Mesa getMesaSel() {
		return mesaSel;
	}

	public void setMesaSel(Mesa mesaSel) {
		this.mesaSel = mesaSel;
	}
}
