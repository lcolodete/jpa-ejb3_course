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

	// Um lance se refere a um único produto

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_ID", nullable=false)
	private Produto produto;

	/*
	 * O  atributo  fetch  indica  se  a  associação  deve ser 
	 * carregada  através da  utilização de  proxy  (de  forma 
	 * "lazy") ou  se ela deve  ser carregada antecipadamente.
	 * Neste caso, o default é EAGER (recuperação antecipada).
	 * 
	 * O elemento @JoinColumn tb é opcional. Se o nome da FK
	 * não for declarada o Hibernate automaticamente utiliza 
	 * a combinação: nome  da entidade alvo (PRODUTO,  neste 
	 * caso)    concatenado    ao   nome   da    propriedade 
	 * identificadora no banco de dados (ID, neste caso). 
	 * 
	 * IMPORTANTE
	 * 
	 * @JoinColumn  especifica  um nome de coluna que deverá
	 * existir na tabela LANCE,  mas o que define se  deverá
	 * haver ou não uma  tabela  PRODUTO_LANCE  para  mapear
	 * este  relacionamento 1 p/ N é a existência  ou não do
	 * atributo  mappedBy  em  Produto  (veja  a  explicação 
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

	// ********* Métodos do Tipo Get *********

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
	
	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setValor(String valor)
		throws AplicacaoException
	{	
		if (valor == null || valor.trim().length() == 0)
		{	throw new AplicacaoException("Valor inválido.");
		}
		else
		{	try
			{	this.valor = Util.strToDouble(valor);
			}
			catch(NumberFormatException e)
			{	throw new AplicacaoException("Valor inválido.");
			}
		}
	}
	
	public void setDataCriacao(String dataCriacao)
		throws AplicacaoException
	{	if (!Util.dataValida(dataCriacao))
		{	throw new AplicacaoException("Data de emissão inválida.");
		}
		else
		{	this.dataCriacao = Util.strToDate(dataCriacao);
		}
	}
	
	// ********* Métodos para Associações *********

	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;
	}
}	