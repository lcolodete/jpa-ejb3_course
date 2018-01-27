package ex03.dataObject;

import java.io.Serializable;

public class PedidoDO implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long idPedido;
	private String nomeCliente;
	private double precoTotal;
	
	public PedidoDO(Long idPedido, String nomeCliente, double precoTotal)
	{	this.idPedido = idPedido;
		this.nomeCliente = nomeCliente;
		this.precoTotal = precoTotal;
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
}

