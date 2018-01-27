package exercicio25.pagamento;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="CONTA")

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
	@Column(name = "BANCO")
	public String getBanco()
	{	return banco;
	}
	
	@Column(name = "AGENCIA")
	public String getAgencia()
	{	return agencia;
	}
	
	@Column(name = "CONTA")
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

