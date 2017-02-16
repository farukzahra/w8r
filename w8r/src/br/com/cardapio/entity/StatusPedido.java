package br.com.cardapio.entity;

import br.com.cardapio.util.Mensagens;

public enum StatusPedido {
	AGUARDANDO_ATENDIMENTO("cm.status.aguard.atend"),
	PROCESSANDO_COZINHA("cm.status.cozinha"),
	AGUARDANDO_ENTREGA("cm.status.aguard.entrega"),
	ENTREGUE("cm.status.entregue"),
	CANCELADO("cm.status.cancelado");
	
	private String msg;
	
	private StatusPedido(String msg){
		this.msg = msg;
	}
	
	public String getTraducao() {
		return Mensagens.getMensagem(msg);
	}

}
