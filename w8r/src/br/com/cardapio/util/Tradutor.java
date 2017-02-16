package br.com.cardapio.util;

import java.util.List;

import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Pedido;
import br.com.cardapio.entity.Pergunta;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.entity.ProdutoPergunta;
import br.com.cardapio.entity.Resposta;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Tradutor {
    
    public static void traduzirProdutos(List<Produto> produtos, Lingua lingua){
    	if(produtos != null)
	        for(Produto produto : produtos){
	        	produto.setLinguaTraduzida(lingua);
	        }
    }
    
    public static void traduzirGrupoProdutos(List<GrupoProduto> grupos, Lingua lingua){
    	if(grupos != null)
	        for(GrupoProduto grupo : grupos){
	        	grupo.setLinguaTraduzida(lingua);
	        }
    }
    
    public static String traduzirAuto(String value, Lingua init, Lingua alvo) throws Exception{
        Translate.setKey("F5F8E5FEBC7CE8F51D88EB6F599A626D7AFDB2AD");
        return Translate.execute(value, Language.fromString(init.getLocale().getLanguage()), 
                Language.fromString(alvo.getLocale().getLanguage()));
    }
    
    public static void traduzirPerguntas(List<Pergunta> perguntas, Lingua lingua){
    	if(perguntas != null)
	        for(Pergunta pergunta : perguntas){
	        	pergunta.setLinguaTraduzida(lingua);
	        }
    }
    
    public static void traduzirRespostas(List<Resposta> respostas, Lingua lingua){
    	if(respostas != null)
	        for(Resposta pergunta : respostas){
	        	pergunta.setLinguaTraduzida(lingua);
	        }
    }
    
    public static void traduzirPedidos(List<Pedido> pedidos, Lingua lingua){
    	if(pedidos != null)
	        for(Pedido pedido : pedidos){
	        	traduzirPedido(pedido, lingua);
	        }
    }

	public static void traduzirPedido(Pedido pedido, Lingua lingua) {
		Produto produto = pedido.getProduto();
		if(produto != null){
			produto.setLinguaTraduzida(lingua);
			List<ProdutoPergunta> prodPergs = produto.getProdutoPerguntas();
			for(ProdutoPergunta prodPerg : prodPergs){
				Pergunta pergunta = prodPerg.getPergunta();
				if(pergunta != null){
					pergunta.setLinguaTraduzida(lingua);
					List<Resposta> respostas = pergunta.getRespostas();
					for(Resposta resposta : respostas)
						resposta.setLinguaTraduzida(lingua);
				}
			}
		}
	}
}
