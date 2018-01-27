package ex05.dataObject;

import java.io.Serializable;

public class PedidoDO implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long idPedido;
	private String nomeCliente;
	private double precoTotal;
	private String numCartao;
	
	public PedidoDO(Long idPedido, String nomeCliente, double precoTotal, String numCartao)
	{	this.idPedido = idPedido;
		this.nomeCliente = nomeCliente;
		this.precoTotal = precoTotal;
		this.numCartao = numCartao;
	}

	public String toString()
	{	return "Pedido = " + idPedido + '\n' +
		       "Nome do Cliente = " + nomeCliente + '\n' + 
		       "Preço total = " + precoTotal + '\n' +
		       "Número do cartão = " + numCartao;
	}
	public Long getIdPedido() 
	{	return idPedido;
	}

	public String getNomeCliente() 
	{	return nomeCliente;
	}

	public double getPrecoTotal() 
	{	return precoTotal;
	}

	public String getNumCartao() 
	{	return numCartao;
	}
}

