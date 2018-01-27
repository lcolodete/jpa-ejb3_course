package exercicio24.pagamento;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CONTA")

public class Conta extends Pagamento      
{	private String banco;
	private String agencia;
	private String conta;
	
	/*****  M�todos Construtores *****/

	public Conta()
	{
	}

	public Conta(double valor, Date data, String banco, String agencia, String conta)
	{	super(valor, data);
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
	}

	/*****  M�todos do tipo get *****/

	public String getBanco()
	{	return banco;
	}
	
	public String getAgencia()
	{	return agencia;
	}
	
	public String getConta()
	{	return conta;
	}
	
	/*****  M�todos do tipo set *****/

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

