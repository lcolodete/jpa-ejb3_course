package exercicio27.lance;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class IdLance implements Serializable
{	
	private final static long serialVersionUID = 1;	

	private long idProduto;
	private long numeroLance;

	public IdLance()
	{
	}

	public IdLance(long idProduto, long numeroLance)
	{	this.idProduto = idProduto;
		this.numeroLance = numeroLance;
	}

//  Métodos do tipo get

	public long getIdProduto()
	{	return idProduto;
	}
	
	public long getNumeroLance()
	{	return numeroLance;
	}
	
//  Métodos do tipo set

	@SuppressWarnings("unused")
	private void setIdProduto(long idProduto)
	{	this.idProduto = idProduto;
	}

	@SuppressWarnings("unused")
	private void setNumeroLance(long numeroLance)
	{	this.numeroLance = numeroLance;
	}
}
