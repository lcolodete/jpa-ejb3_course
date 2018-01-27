package ex04.dominio;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="PEDIDO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PEDIDO",
		           allocationSize=1)

public class Pedido implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date dataEmissao;
	private Cliente cliente;
	private Pagamento pagamento;
	private List<ItemDePedido> itensDePedidos = new ArrayList<ItemDePedido>();

	// ********* Construtores *********

	public Pedido()
	{
	}

	public Pedido(Date dataEmissao)
	{	this.dataEmissao = dataEmissao;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}

	@Column(name="DATA_EMISSAO", nullable = false)
	public Date getDataEmissao()
	{	return dataEmissao;
	}
	
	@Transient
	public double getPrecoTotal()
	{	double precoTotal = 0;
		for (Iterator<ItemDePedido> it = itensDePedidos.iterator(); it.hasNext();)
		{	ItemDePedido ip = (ItemDePedido) it.next();
			precoTotal = precoTotal + ip.getQtd() * ip.getProduto().getPreco();
		}
		return precoTotal;
	}
	
	
	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setDataEmissao(Date dataEmissao)
	{	this.dataEmissao = dataEmissao;
	}
	
// ********* Métodos para Associações *********

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CLIENTE_ID")
	public Cliente getCliente()
	{	return cliente;
	}
	
	public void setCliente(Cliente cliente)
	{	this.cliente = cliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PAGAMENTO_ID")
	public Pagamento getPagamento()
	{	return pagamento;
	}

	public void setPagamento(Pagamento pagamento)
	{	this.pagamento = pagamento;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
			   mappedBy = "pedido")
	@OrderBy
	public List<ItemDePedido> getItensDePedidos()
	{	return itensDePedidos;
	}
	
	public void setItensDePedidos(List<ItemDePedido> itensDePedidos)
	{	this.itensDePedidos = itensDePedidos;	
	}
	
	public void adicionarProduto(Produto produto, int qtd)
	{	itensDePedidos.add(new ItemDePedido(qtd, produto, this));
	}
}

