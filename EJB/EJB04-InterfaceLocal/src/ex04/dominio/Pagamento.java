package ex04.dominio;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PAGAMENTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PAGAMENTO",
		           allocationSize=1)

public class Pagamento implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date dataPagamento;
	private String numCartao;
	private double valor;

	// ********* Construtores *********

	public Pagamento()
	{
	}

	public Pagamento(Date dataPagamento, String numCartao, double valor)
	{	this.dataPagamento = dataPagamento;
		this.numCartao = numCartao;
		this.valor = valor;
	}
	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}

	@Column(name="DATA_PAGAMENTO", nullable = false)
	public Date getDataPagamento()
	{	return dataPagamento;
	}
	
	@Column(name="NUM_CARTAO", nullable = false)
	public String getNumCartao() 
	{	return numCartao;
	}

	public double getValor() 
	{	return valor;
	}

	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setDataPagamento(Date dataPagamento)
	{	this.dataPagamento = dataPagamento;
	}

	public void setNumCartao(String numCartao) 
	{	this.numCartao = numCartao;
	}

	public void setValor(double valor) 
	{	this.valor = valor;
	}
}

