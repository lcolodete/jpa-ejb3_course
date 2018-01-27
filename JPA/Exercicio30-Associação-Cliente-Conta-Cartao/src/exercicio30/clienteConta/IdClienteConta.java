package exercicio30.clienteConta;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IdClienteConta implements Serializable
{	
	private final static long serialVersionUID = 1;	

	private long idCliente;
	private long idConta;

	public IdClienteConta()
	{
	}

	public IdClienteConta(long idCliente, long idConta)
	{	this.idCliente = idCliente;
		this.idConta = idConta;
	}
	
	@SuppressWarnings("unused")
	@Column(name="ID_CLIENTE")
	public long getIdCliente()
	{	return idCliente;
	}

	@SuppressWarnings("unused")
	@Column(name="ID_CONTA")
	public long getIdConta()
	{	return idConta;
	}

	public void setIdCliente(long idCliente)
	{	this.idCliente = idCliente;
	}

	public void setIdConta(long idConta)
	{	this.idConta = idConta;
	}
}
