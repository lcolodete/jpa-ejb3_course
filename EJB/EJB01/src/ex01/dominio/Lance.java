package ex01.dominio;

import java.io.Serializable;
import java.sql.Date;

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

import ex01.util.Util;


@Entity
@Table(name="LANCE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_LANCE",
		           allocationSize=1)

public class Lance implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private Long id;
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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_ID")
	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;
	}
}	