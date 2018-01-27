package exercicio27.lance;

import java.sql.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import exercicio27.produto.Produto;
import exercicio27.util.Util;


//                              A T E N � � O 

// Na busca JPQL n�o funciona especificar "where l.id = ?"  sendo o id  do tipo 
// IdLance. � preciso especificar cada valor de IdLance separadamente, conforme
// vem abaixo:
//
//        where l.id.idProduto = ? and l.id.numeroLance = ?
//
// N�o  funciona  com  "where l.id = ?"  uma  vez  que  IdLance � um objeto e a 
// compara��o  seria com  o endere�o de  mem�ria deste  objeto e  n�o com  seus 
// valores. 

@NamedQueries(
		{	@NamedQuery
			(	name = "Lance.recuperaUmLanceComProduto",
				query = "select l from Lance l left outer join fetch l.produto " + 
				        "where l.id.idProduto = ? and l.id.numeroLance = ?"
			),
			@NamedQuery
			(	name = "Lance.recuperaListaDeLances",
				query = "select l from Lance l order by l.id"
			)
		})

@Entity
@Table(name="LANCE")

public class Lance
{	private IdLance id;
	private double valor;
	private Date dataCriacao;
	private Produto produto;

	// ********* Construtores *********

	public Lance()
	{
	}

	public Lance(double valor, Date dataCriacao)
	{	this.valor = valor;
		this.dataCriacao = dataCriacao;
	}

	// ********* M�todos do Tipo Get *********

	@EmbeddedId  // Para indicar que o atributo id n�o  deve
	             // ser mapeado para a coluna ID no banco de
	             // dados.
	@AttributeOverrides
	({
		@AttributeOverride(name = "idProduto",
				           column = @Column(name="ID_PRODUTO")),
		@AttributeOverride(name = "numeroLance",
				           column = @Column(name="NUMERO_LANCE")),				           
	})

	public IdLance getId()
	{	return id;
	}
	
	@Column(nullable = false)
	public double getValor()
	{	return valor;
	}
	
	@Column(name="DATA_CRIACAO", nullable = false)
	public Date getDataCriacao()
	{	return dataCriacao;
	}
	
	@Transient
	public String getDataCriacaoMasc()
	{	return Util.dateToStr(dataCriacao);
	}
	
	// ********* M�todos do Tipo Set *********

	public void setId(IdLance id)
	{	this.id = id;
	}
	
	public void setValor(double valor)
	{	this.valor = valor;
	}

	public void setDataCriacao(Date dataCriacao)
	{	this.dataCriacao = dataCriacao;
	}

	// ********* M�todos para Associa��es *********

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_PRODUTO",
			    insertable=false,
			    updatable=false)
	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;
	}
}	
