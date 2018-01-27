package exercicio10.lance;

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

import exercicio10.produto.Produto;
import exercicio10.util.*;

@Entity
@Table(name="LANCE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_LANCE",
		           allocationSize=1)

public class Lance
{	private Long id;
	private double valor;
	private Date dataCriacao;

	// Um lance se refere a um �nico produto

	private Produto produto;

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

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")

	public Long getId()
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

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setValor(double valor)
	{	this.valor = valor;
	}

	public void setDataCriacao(Date dataCriacao)
	{	this.dataCriacao = dataCriacao;
	}

	// ********* M�todos para Associa��es *********

	/*
	 * O  atributo  fetch  indica  se  a  associa��o  deve ser 
	 * carregada  atrav�s da  utiliza��o de  proxy  (de  forma 
	 * "lazy") ou  se ela deve  ser carregada antecipadamente.
	 * Neste caso, o default � EAGER (recupera��o antecipada).
	 * 
	 * O elemento @JoinColumn tb � opcional. Se o nome da  FK
	 * n�o  for  declarada  a  JPA   automaticamente  utiliza 
	 * a combina��o: nome  da  entidade alvo (PRODUTO,  neste 
	 * caso)    concatenado    ao   nome   da     propriedade 
	 * identificadora no banco de dados (ID, neste caso). 
	 * 
	 * IMPORTANTE
	 * 
	 * @JoinColumn  especifica  o nome  da  coluna  (FK)  que 
	 * dever� existir na tabela LANCE,  mas o que  define  se
	 * dever� haver  ou  n�o uma  tabela  PRODUTO_LANCE  para
	 * mapear este  relacionamento 1 p/ N � a exist�ncia   ou
	 * n�o   do   atributo   mappedBy  em   Produto  (veja  a
	 * explica��o existente em Produto -> no exerc�cio 9)
	 */

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_ID", nullable=false)
	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;
	}
}	