package exercicio22.cliente;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Endereco
{	private String rua;
	private String numero;
	private String complemento;

	/*****  Métodos Construtores *****/
	
	public Endereco()
	{
	}

	public Endereco(String rua, String numero, String complemento)
	{	this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
	}

	/*****  Métodos do tipo get *****/

	@Column(name = "RUA")
	public String getRua()
	{	return rua;
	}
	
	@Column(name = "NUM")
	public String getNumero()
	{	return numero;
	}
	
	@Column(name = "COMPLEMENTO")
	public String getComplemento()
	{	return complemento;
	}
	
	/*****  Métodos do tipo set *****/

	public void setRua(String rua)
	{	this.rua = rua;
	}
	
	public void setNumero(String numero)
	{	this.numero = numero;
	}

	public void setComplemento(String complemento)
	{	this.complemento = complemento;
	}
}
