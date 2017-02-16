package br.com.cardapio.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.ProdutoPergunta;
import br.com.cardapio.entity.Resposta;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.CryptMD5;

public class EmpresaBO extends BOBase<Empresa> {
    
    private static final long serialVersionUID = 1L;

    public EmpresaBO() {
        super();
        setClazz(Empresa.class);
    }
    
    public List<Empresa> buscarAtivas() throws BancoDadosException{        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("status", Empresa.STATUS_ATIVO); 
        return listByFields(parametros); 
    }
    
    public Empresa findByEmail(String email) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("email", email); 
        return findByFields(parametros); 
    }
    
    public Empresa findByEmailESenha(String email, String senha) throws BancoDadosException {        
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("email", email);                   
        parametros.put("senha", senha);
        return findByFields(parametros);
    }
    
    public List<Empresa> listByDistancia(double latitude, double longitude) throws BancoDadosException{
    	List<Empresa> empresasSort = new ArrayList<Empresa>();
    	List<Empresa> empresas = buscarAtivas();
		for(Empresa empresa : empresas){				
			double latEmp = empresa.getLatitude();
			double lonEmp = empresa.getLongitude();
			double distancia = Math.sqrt(Math.pow(latEmp - latitude, 2) + Math.pow(lonEmp - longitude, 2));
			empresa.setDistancia(distancia);
			//Valida se está a 50km de distancia
			//if(distancia < 0.5){					
				empresasSort.add(empresa);
		    //}
		}
		Collections.sort(empresasSort);
		if(empresasSort.size() > 10)
			empresasSort = empresasSort.subList(0, 9);
		/*boolean houveTroca = true;
		while (houveTroca == true) {
            houveTroca = false;
            for(int i = 0; i < empresasSort.size()-1; i++){
                if (empresasSort.get(i).getDistancia() > empresasSort.get(i+1).getDistancia()){
                    Empresa variavelAuxiliar = empresasSort.get(i+1);
                    empresasSort.set(i+1, empresasSort.get(i));
                    empresasSort.set(i, variavelAuxiliar);
                    houveTroca = true;
                }
            }
		}*/
		return empresasSort;    	
    }
    
    public Empresa findByEmailValidandoSenha(String email, String senha){
    	Empresa empresa = null;    	
        try {
        	if(email != null && !email.isEmpty() && senha != null && !senha.isEmpty()){
	        	empresa = findByEmailESenha(email,CryptMD5.encrypt(senha));
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }  
    	return empresa;
    }
    
    public void cadastrarDadosEmpresa(Empresa empresa){
    	GrupoProdutoBO grupoProdutoBO = new GrupoProdutoBO();
		ProdutoBO produtoBO = new ProdutoBO();
		PerguntaBO perguntaBO = new PerguntaBO();
		RespostaBO respostaBO = new RespostaBO();
		MesaBO mesaBO = new MesaBO();
		ProdutoPerguntaBO produtoPerguntaBO = new ProdutoPerguntaBO();
		try {
			for (int i = 1; i < 11; i++) {
				mesaBO.persist(new Mesa(i,i+"",empresa));	
			}
			GrupoProduto bebida = new GrupoProduto("Bebidas", empresa, "bebidas.jpg");
			GrupoProduto comida = new GrupoProduto("Comidas", empresa, "comidas.jpg");
			
			grupoProdutoBO.persist(bebida);			
			grupoProdutoBO.persist(comida);
			
			copiaImgGrupo(bebida);
			copiaImgGrupo(comida);			
			
			Produto skol = new Produto("Chop", "Chop" , new Double(7), bebida, true, "chop.jpg", "chop_peq.jpg");
			Produto coca = new Produto("Coca Cola", "Coca Cola" , new Double(4.5), bebida, true, "coca.jpg", "coca_peq.jpg");
			Produto h2o = new Produto("H2O", "H2O" , new Double(5), bebida, true, "h2o.jpg", "h2o_peq.jpg");
			
			
			Produto batata = new Produto("Brusqueta", "Brusqueta" , new Double(15), comida, true, "brusqueta.jpg", "brusqueta_peq.jpg");
			Produto hamburguer = new Produto("Hamburguer", "Hamburguer" , new Double(7), comida, true, "hamburguer.jpg", "hamburguer_peq.jpg");			
			Produto bolinho = new Produto("Bolinho de Arroz", "Bolinho de Arroz" , new Double(12), comida, true, "bolinho.jpg", "bolinho_peq.jpg");
			
			produtoBO.persist(skol);			
			produtoBO.persist(coca);
			produtoBO.persist(h2o);			
			produtoBO.persist(batata);
			produtoBO.persist(hamburguer);
			produtoBO.persist(bolinho);
			
			copiaImgProduto(skol);
			copiaImgProduto(coca);
			copiaImgProduto(h2o);
			copiaImgProduto(batata);
			copiaImgProduto(hamburguer);
			copiaImgProduto(bolinho);
			
			
			Pergunta limao = new Pergunta("Limão no copo?", Pergunta.TIPO_RESPOSTA_SIM_OU_NAO,empresa);
			Pergunta qualSabor = new Pergunta("Qual Sabor?", Pergunta.TIPO_RESPOSTA_UMA_EM_VARIAS,empresa);
			
			perguntaBO.persist(limao);
			perguntaBO.persist(qualSabor);
			
			
			Resposta r1 = new Resposta("Limão", qualSabor);
			Resposta r2 = new Resposta("Maça", qualSabor);
			Resposta r3 = new Resposta("Citrus", qualSabor);
			
			respostaBO.persist(r1);
			respostaBO.persist(r2);
			respostaBO.persist(r3);
			
			produtoPerguntaBO.persist(new ProdutoPergunta(qualSabor, h2o));
			produtoPerguntaBO.persist(new ProdutoPergunta(limao, h2o));			
			produtoPerguntaBO.persist(new ProdutoPergunta(limao, coca));			
			produtoPerguntaBO.persist(new ProdutoPergunta(limao, skol));			
		} catch (BancoDadosException e) {
			e.printStackTrace();
		} catch (IntegridadeReferencialException e) {
			e.printStackTrace();
		} catch (RegistroExistenteException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void copiaImgProduto(Produto produto) throws Exception{
    	String pathImagemDetalhada = produto.getPathImagemDetalhada();
    	String pathImagemPrincipal = produto.getPathImagemPrincipal();
    	
    	File d = new File("c:\\tmp\\fixas\\"+pathImagemDetalhada);
    	FileUtils.copyFile(d, new File("c:\\tmp\\imagens\\imgProduto\\Empr_" + produto.getGrupoProduto().getEmpresa().getId() + "_Prd_" + produto.getId() + "." + pathImagemDetalhada.split("\\.")[1]));
    	
    	File p = new File("c:\\tmp\\fixas\\"+pathImagemPrincipal);
    	FileUtils.copyFile(d, new File("c:\\tmp\\imagens\\imgProduto\\Empr_" + produto.getGrupoProduto().getEmpresa().getId() + "_Prd_" + produto.getId() + "_peq_." + pathImagemPrincipal.split("\\.")[1]));
    	

    	produto.setPathImagemDetalhada("/imgProduto/Empr_" + produto.getGrupoProduto().getEmpresa().getId() + "_Prd_" + produto.getId() + "." + pathImagemDetalhada.split("\\.")[1]);
    	
    	produto.setPathImagemPrincipal("/imgProduto/Empr_" + produto.getGrupoProduto().getEmpresa().getId() + "_Prd_" + produto.getId() + "_peq_." + pathImagemDetalhada.split("\\.")[1]);
    	new ProdutoBO().persist(produto);
	}
    
    public void copiaImgGrupo(GrupoProduto produto) throws Exception{
    	String pathImagem = produto.getPathImagem();
    	
    	File d = new File("c:\\tmp\\fixas\\"+pathImagem);
    	FileUtils.copyFile(d, new File("c:\\tmp\\imagens\\imgGrupoProduto\\Empr_" + produto.getEmpresa().getId() + "_GrpPrd_" + produto.getId() + "." + pathImagem.split("\\.")[1]));
    	
    	produto.setPathImagem("/imgGrupoProduto/Empr_" + produto.getEmpresa().getId() + "_GrpPrd_" + produto.getId() + "." + pathImagem.split("\\.")[1]);
    	new GrupoProdutoBO().persist(produto);
	}

	public static void main(String[] args){
//    	EmpresaBO bo = new EmpresaBO();
//    	Produto produto = new Produto();
//    	produto.setPathImagemDetalhada("coca.jpg");
//    	produto.setPathImagemPrincipal("coca_peq.jpg");
//    	produto.setId(10);
//    	GrupoProduto grupoProduto = new GrupoProduto();
//    	grupoProduto.setId(1);
//    	Empresa empresa = new Empresa();
//    	empresa.setId(1);
//		grupoProduto.setEmpresa(empresa );
//		produto.setGrupoProduto(grupoProduto);
//		try {
//			bo.copiaImgProduto(produto );
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		//}
//    	Empresa novaEmpresa = new Empresa();
//    	novaEmpresa.setNome("Matriz & Filial");
//    	novaEmpresa.setDescricao("Bar Matriz & Filial bla bla");
//    	novaEmpresa.setStatus('A');
//    	novaEmpresa.setEmail("matriz");
//    	novaEmpresa.setSenha("matriz");
    	
    	try {
    		EmpresaBO bo = new EmpresaBO();
    		Empresa novaEmpresa = bo.findByEmail("rteste");
    		novaEmpresa.setEmail("rt");
    		novaEmpresa.setSenha(CryptMD5.encrypt("1"));
			bo.persist(novaEmpresa);
		
//	    	List<Empresa> empresas = bo.listAll();
//	    	System.out.println(empresas);
//	    	bo.find(novaEmpresa);
//	    	Empresa empresaLogada = bo.findByEmailValidandoSenha("matriz", "matriz");
//	    	System.out.println(empresaLogada);
    	} catch (BancoDadosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntegridadeReferencialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RegistroExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}