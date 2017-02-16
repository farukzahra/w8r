package br.com.cardapio.util;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.bo.GrupoProdutoBO;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PerguntaBO;
import br.com.cardapio.bo.ProdutoBO;
import br.com.cardapio.bo.ProdutoPerguntaBO;
import br.com.cardapio.bo.RespostaBO;
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

public class CadastraEmpresa {

	public static void main(String[] args) {
		cadastrarEmpresa(args);
	}

	private static void deletarEmpresa(String[] args) {
		try {
			/**
			delete from produto_pergunta where id_produto in (select id_produto from produto p, grupo_produto g where p.id_grupo_produto = g.id_grupo_produto and g.id_empresa = 157);
			delete from resposta where id_pergunta in (select id_pergunta from pergunta where id_empresa = 157);
			delete from pergunta where id_empresa = 157;
			select id_produto from produto where id_produto in (select id_produto from produto p, grupo_produto g where p.id_grupo_produto = g.id_grupo_produto and g.id_empresa = 157);
			
			delete from produto  where id_produto in (71,72,73,74,75,76)
			
			delete from grupo_produto where id_empresa = 157;
			delete from mesa where id_empresa = 157;
			delete from empresa where id_empresa = 157;
			**/
			
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}

	
	private static void cadastrarEmpresa(String[] args) {

		Empresa empresa = new Empresa(args[0],args[1],args[2],args[3], true, true, true, true);		
		EmpresaBO empresaBO = new EmpresaBO();
		GrupoProdutoBO grupoProdutoBO = new GrupoProdutoBO();
		ProdutoBO produtoBO = new ProdutoBO();
		PerguntaBO perguntaBO = new PerguntaBO();
		RespostaBO respostaBO = new RespostaBO();
		MesaBO mesaBO = new MesaBO();
		ProdutoPerguntaBO produtoPerguntaBO = new ProdutoPerguntaBO();
		try {
			empresaBO.persist(empresa);
			System.out.println(empresa.getId());
			for (int i = 1; i < 11; i++) {
				mesaBO.persist(new Mesa(i,i+"",empresa));	
			}
			GrupoProduto bebida = new GrupoProduto("Bebidas", empresa, "/imgGrupoProduto/bebidas.jpg");
			grupoProdutoBO.persist(bebida);
			GrupoProduto comida = new GrupoProduto("Comidas", empresa, "/imgGrupoProduto/comidas.jpg");
			grupoProdutoBO.persist(comida);
			
			Produto skol = new Produto("Chop", "Chop" , new Double(7), bebida, true, "/imgProduto/chop.jpg", "/imgProduto/chop_peq.jpg");
			Produto coca = new Produto("Coca Cola", "Coca Cola" , new Double(4.5), bebida, true, "/imgProduto/coca.jpg", "/imgProduto/coca_peq.jpg");
			Produto h2o = new Produto("H2O", "H2O" , new Double(5), bebida, true, "/imgProduto/h2o.jpg", "/imgProduto/h2o_peq.jpg");
			
			
			Produto batata = new Produto("Brusqueta", "Brusqueta" , new Double(15), comida, true, "/imgProduto/brusqueta.jpg", "/imgProduto/brusqueta_peq.jpg");
			Produto hamburguer = new Produto("Hamburguer", "Hamburguer" , new Double(7), comida, true, "/imgProduto/hamburguer.jpg", "/imgProduto/hamburguer_peq.jpg");			
			Produto bolinho = new Produto("Bolinho de Arroz", "Bolinho de Arroz" , new Double(12), comida, true, "/imgProduto/bolinho.jpg", "/imgProduto/bolinho_peq.jpg");
			
			produtoBO.persist(skol);
			produtoBO.persist(coca);
			produtoBO.persist(h2o);
			
			produtoBO.persist(batata);
			produtoBO.persist(hamburguer);
			produtoBO.persist(bolinho);
			
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
		}
	}
}