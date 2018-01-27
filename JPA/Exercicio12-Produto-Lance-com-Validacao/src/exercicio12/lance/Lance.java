package exercicio12.lance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.sql.Date;

import exercicio12.produto.Produto;
import exercicio12.util.*;

@Entity
@Table(name="LANCE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_LANCE",
		           allocationSize=1)

public class Lance
{	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	private Long id;

	@Column(nullable = false)
	private double valor;
	
	@Column(name="DATA_CRIACAO", nullable = false)
	private Date dataCriacao;

	// Um lance se refere a um �nico produto

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_ID", nullable=false)
	private Produto produto;

	/*
	 * O  atributo  fetch  indica  se  a  associa��o  deve ser 
	 * carregada  atrav�s da  utiliza��o de  proxy  (de  forma 
	 * "lazy") ou  se ela deve  ser carregada antecipadamente.
	 * Neste caso, o default � EAGER (recupera��o antecipada).
	 * 
	 * O elemento @JoinColumn tb � opcional. Se o nome da FK
	 * n�o for declarada o Hibernate automaticamente utiliza 
	 * a combina��o: nome  da entidade alvo (PRODUTO,  neste 
	 * caso)    concatenado    ao   nome   da    propriedade 
	 * identificadora no banco de dados (ID, neste caso). 
	 * 
	 * IMPORTANTE
	 * 
	 * @JoinColumn  especifica  um nome de coluna que dever�
	 * existir na tabela LANCE,  mas o que define se  dever�
	 * haver ou n�o uma  tabela  PRODUTO_LANCE  para  mapear
	 * este  relacionamento 1 p/ N � a exist�ncia  ou n�o do
	 * atributo  mappedBy  em  Produto  (veja  a  explica��o 
	 * existente em Produto)
	 */

	
	// ********* Construtores *********

	public Lance()
	{
	}

	public Lance(double valor, Date dataCriacao, Produto produto)
	{	this.valor = valor;
		this.dataCriacao = dataCriacao;
		this.produto = produto;
	}

	// ********* M�todos do Tipo Get *********

	public Long getId()
	{	return id;
	}

	public double getValor()
	{	return valor;
	}
	
	public Date getDataCriacao()
	{	return dataCriacao;
	}
	
	@Transient
	public String getDataCriacaoMasc()
	{	return Util.dateToStr(dataCriacao);
	}
	
	// ********* M�todos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setValor(String valor)
		throws AplicacaoException
	{	
		if (valor == null || valor.trim().length() == 0)
		{	throw new AplicacaoException("Valor inv�lido.");
		}
		else
		{	try
			{	this.valor = Util.strToDouble(valor);
			}
			catch(NumberFormatException e)
			{	throw new AplicacaoException("Valor inv�lido.");
			}
		}
	}
	
	public void setDataCriacao(String dataCriacao)
		throws AplicacaoException
	{	if (!Util.dataValida(dataCriacao))
		{	throw new AplicacaoException("Data de emiss�o inv�lida.");
		}
		else
		{	this.dataCriacao = Util.strToDate(dataCriacao);
		}
	}
	
	// ********* M�todos para Associa��es *********

	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;
	}
}	