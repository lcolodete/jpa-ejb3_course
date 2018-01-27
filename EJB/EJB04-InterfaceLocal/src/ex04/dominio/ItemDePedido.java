package ex04.dominio;

import java.io.Serializable;

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

@Entity
@Table(name="ITEM_PEDIDO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_ITEM_PEDIDO",
		           allocationSize=1)

public class ItemDePedido implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long id;
	private int qtd;
	private Produto produto;
	private Pedido pedido;
	// ********* Construtores *********

	public ItemDePedido()
	{
	}

	public ItemDePedido(int qtd, Produto produto, Pedido pedido)
	{	this.qtd = qtd;
		this.produto = produto;
		this.pedido = pedido;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}
	
	@Column(nullable = false)
	public int getQtd()
	{	return qtd;
	}
	
	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setQtd(int qtd)
	{	this.qtd = qtd;
	}
	
// ********* Métodos para Associações *********

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PEDIDO_ID", nullable=false)

	public Pedido getPedido()
	{	return pedido;
	}
	
	public void setPedido(Pedido pedido)
	{	this.pedido = pedido;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_ID", nullable=false)

	public Produto getProduto()
	{	return produto;
	}
	
	public void setProduto(Produto produto)
	{	this.produto = produto;	
	}
}

