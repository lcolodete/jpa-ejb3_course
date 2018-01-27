package exercicio24.pagamento;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CARTAO")

public class Cartao extends Pagamento       
{	private String numero;
	private String mesExp;
	private String anoExp;
	
	/*****  Métodos Construtores *****/

	public Cartao()
	{
	}

	public Cartao(double valor, Date data, String numero, String mesExp, String anoExp)
	{	super(valor, data);
		this.numero = numero;
		this.mesExp = mesExp;
		this.anoExp = anoExp;
	}

	/*****  Métodos do tipo get *****/

	@Column(name="NUMERO")
	public String getNumero()
	{	return numero;
	}
	
	@Column(name="MES_EXP")
	public String getMesExp()
	{	return mesExp;
	}
	
	@Column(name="ANO_EXP")
	public String getAnoExp()
	{	return anoExp;
	}
	
	/*****  Métodos do tipo set *****/

	public void setNumero(String numero)
	{	this.numero = numero;
	}
	
	public void setMesExp(String mesExp)
	{	this.mesExp = mesExp;
	}

	public void setAnoExp(String anoExp)
	{	this.anoExp = anoExp;
	}
}

