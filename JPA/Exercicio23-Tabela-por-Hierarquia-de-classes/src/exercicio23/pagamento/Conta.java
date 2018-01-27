package exercicio23.pagamento;

import java.sql.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CTA")

public class Conta extends Pagamento      
{	private String banco;
	private String agencia;
	private String conta;
	
	/*****  Métodos Construtores *****/

	public Conta()
	{
	}

	public Conta(double valor, Date data, String banco, String agencia, String conta)
	{	super(valor, data);
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
	}

	/*****  Métodos do tipo get *****/

	public String getBanco()
	{	return banco;
	}
	
	public String getAgencia()
	{	return agencia;
	}
	
	public String getConta()
	{	return conta;
	}
	
	/*****  Métodos do tipo set *****/

	public void setBanco(String banco)
	{	this.banco = banco;
	}
	
	public void setAgencia(String agencia)
	{	this.agencia = agencia;
	}

	public void setConta(String conta)
	{	this.conta = conta;
	}
}

