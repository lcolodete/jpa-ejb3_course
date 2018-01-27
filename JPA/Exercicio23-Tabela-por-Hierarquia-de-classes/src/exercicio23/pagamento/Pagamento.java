package exercicio23.pagamento;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(
		{	@NamedQuery
			(	name = "Pagamento.recuperaUmPagamentoEmCartao",
					query = "select c from Cartao c where c.id=?"
			),
			@NamedQuery
			(	name = "Pagamento.recuperaListaDePagamentos",
				query = "select p from Pagamento p order by id"
			)
		})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name = "TIPO_PGTO",
		discriminatorType = DiscriminatorType.STRING)
		
@Table(name="PAGAMENTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PAGAMENTO",
		           allocationSize=1)

public abstract class Pagamento     
{	private Long id;
	private Date data;
	private double valor;

	/*****  Métodos Construtores *****/
	
	public Pagamento()
	{
	}

	public Pagamento(double valor, Date data)
	{	this.valor = valor;
		this.data = data;
	}

	/*****  Métodos do tipo get *****/

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
	@Column(name="ID")

	public Long getId()
	{	return id;
	}

	@Column(name="DATA_PGTO", nullable = false)
	public Date getData()
	{	return data;
	}
	
	@Column(name="VALOR", nullable = false)
	public double getValor()
	{	return valor;
	}
	
	/*****  Métodos do tipo set *****/

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}
	
	public void setData(Date data)
	{	this.data = data;
	}

	public void setValor(double valor)
	{	this.valor = valor;
	}
}

